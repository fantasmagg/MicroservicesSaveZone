import os
import signal
import sys
from flask_cors import CORS
from flask import Flask, request, jsonify
import pandas as pd
import re
from tld import get_tld
from joblib import load
import py_eureka_client.eureka_client as eureka_client
import logging
from pymongo import MongoClient
from bson.objectid import ObjectId

app = Flask(__name__)
CORS(app, resources={
    r"/*": {"origins": "*"}})  # Permite todas las solicitudes desde cualquier origen, cambia esto según sea necesario

# Configuración de Eureka usando py_eureka_client
eureka_client.init(
    eureka_server="http://localhost:8761/eureka",
    app_name="python-api",
    instance_port=5000,
    instance_ip="127.0.0.1",
    renewal_interval_in_secs=10,
    duration_in_secs=30
)

# Configurar logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# Configuración de MongoDB
client = MongoClient('mongodb://localhost:27017/')
db = client['url_db']
url_collection = db['urls']


# Definir funciones de extracción de características
def having_ip_address(url):
    match = re.search(
        r'(([01]?\d\d?|2[0-4]\d|25[0-5])\.([01]?\d\d?|2[0-4]\d|25[0-5])\.)',
        url)
    return 1 if match else 0


def abnormal_url(url):
    try:
        hostname = url.split('/')[2]
        match = re.search(re.escape(hostname), url)
        return 1 if match else 0
    except IndexError:
        return 0


def count_dot(url):
    return url.count('.')


def count_www(url):
    return url.count('www')


def count_atrate(url):
    return url.count('@')


def no_of_dir(url):
    return url.count('/')


def no_of_embed(url):
    return url.count('//')


def shortening_service(url):
    match = re.search(
        r'bit\.ly|goo\.gl|shorte\.st|go2l\.ink|x\.co|ow\.ly|t\.co|tinyurl|tr\.im|is\.gd|cli\.gs|yfrog\.com|migre\.me|ff\.im|tiny\.cc|url4\.eu|twit\.ac|su\.pr|twurl\.nl|snipurl\.com|short\.to|BudURL\.com|ping\.fm|post\.ly|Just\.as|bkite\.com|snipr\.com|fic\.kr|loopt\.us|doiop\.com|short\.ie|kl\.am|wp\.me|rubyurl\.com|om\.ly|to\.ly|bit\.do|t\.co|lnkd\.in|db\.tt|qr\.ae|adf\.ly|bitly\.com|cur\.lv|tinyurl\.com|ow\.ly|bit\.ly|ity\.im|q\.gs|is\.gd|po\.st|bc\.vc|twitthis\.com|u\.to|j\.mp|buzurl\.com|cutt\.us|u\.bb|yourls\.org|prettylinkpro\.com|scrnch\.me|filoops\.info|vzturl\.com|qr\.net|1url\.com|tweez\.me|v\.gd|tr\.im|link\.zip\.net',
        url)
    return 1 if match else 0


def count_https(url):
    return url.count('https')


def count_http(url):
    return url.count('http')


def count_per(url):
    return url.count('%')


def count_ques(url):
    return url.count('?')


def count_hyphen(url):
    return url.count('-')


def count_equal(url):
    return url.count('=')


def url_length(url):
    return len(url)


def hostname_length(url):
    try:
        return len(url.split('/')[2])
    except IndexError:
        return 0


def suspicious_words(url):
    return int('security' in url or 'confirm' in url or 'bank' in url)


def digit_count(url):
    digits = [i for i in url if i.isdigit()]
    return len(digits)


def letter_count(url):
    letters = [i for i in url if i.isalpha()]
    return len(letters)


def fd_length(url):
    try:
        return len(url.split('/')[3])
    except IndexError:
        return 0


def tld_length(tld):
    return len(tld) if tld else 0


# Mapear etiquetas de texto a valores numéricos
label_mapping = {'benign': 0, 'defacement': 1, 'phishing': 2, 'malware': 3}

# Cargar el modelo
model = load('model_filenameG.joblib')


def predict_url(url, model, label_mapping):
    features = pd.Series([
        having_ip_address(url),
        abnormal_url(url),
        count_dot(url),
        count_www(url),
        count_atrate(url),
        no_of_dir(url),
        no_of_embed(url),
        shortening_service(url),
        count_https(url),
        count_http(url),
        count_per(url),
        count_ques(url),
        count_hyphen(url),
        count_equal(url),
        url_length(url),
        hostname_length(url),
        suspicious_words(url),
        digit_count(url),
        letter_count(url),
        fd_length(url),
        tld_length(get_tld(url, fail_silently=True))
    ])
    features = features.values.reshape(1, -1)
    prediction = model.predict(features)
    probabilities = model.predict_proba(features)
    reverse_label_mapping = {v: k for k, v in label_mapping.items()}
    predicted_class = reverse_label_mapping[prediction[0]]
    probabilities_dict = {reverse_label_mapping[i]: format(prob, '.6f') for i, prob in enumerate(probabilities[0])}
    return predicted_class, probabilities_dict


@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        url = data['url']

        predicted_class, probabilities = predict_url(url, model, label_mapping)

        # Verificar si la URL ya está en la base de datos
        existing_url = url_collection.find_one({"url": url})
        if existing_url:
            # Incrementar el contador si la URL ya existe
            url_collection.update_one({"_id": existing_url["_id"]}, {"$inc": {"count": 1}})
        else:
            # Insertar nueva URL con contador inicial de 1 y almacenar la clasificación
            url_collection.insert_one({"url": url, "count": 1, "classification": predicted_class})

        return jsonify({'url': url, 'prediction': predicted_class, 'probabilities': probabilities})
    except Exception as e:
        logger.error(f"Error en la predicción: {e}")
        return jsonify({'error': 'Prediction failed', 'message': str(e)}), 500


@app.route('/scanned_urls', methods=['GET'])
def get_scanned_urls():
    try:
        # Obtener todas las URLs de la base de datos
        urls = url_collection.find({}, {"_id": 0, "url": 1, "count": 1, "classification": 1})
        # Convertir los resultados a una lista de diccionarios
        urls_list = list(urls)
        return jsonify(urls_list)
    except Exception as e:
        logger.error(f"Error al obtener las URLs escaneadas: {e}")
        return jsonify({'error': 'Failed to fetch scanned URLs', 'message': str(e)}), 500


def handle_shutdown(signum, frame):
    logger.info("Se recibió señal de terminación, desregistrando de Eureka...")
    try:
        eureka_client.stop()
    except Exception as e:
        logger.error(f"Error al desregistrar de Eureka: {e}")
    finally:
        os._exit(0)


if __name__ == '__main__':
    signal.signal(signal.SIGTERM, handle_shutdown)
    signal.signal(signal.SIGINT, handle_shutdown)
    app.run(host='0.0.0.0', port=5000)
