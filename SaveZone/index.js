//FILE INPUT

            document.getElementById('fileInput').addEventListener('change', async (event) => {
                const file = event.target.files[0];
                if (file) {
                    const formData = new FormData();
                    formData.append('file', file);
    
                    try {
                        const response = await fetch('http://localhost:9090/api/virus/files', {
                            method: 'POST',
                            body: formData,
                        });
                        const result = await response.json();
                        const id = result.data.id;
                        console.log('ID:', id);
    
                        // Consumir la segunda ruta con el ID obtenido
                        const analysisResponse = await fetch(`http://localhost:9090/api/virus/analyses/${id}`, {
                            method: 'GET'
                        });
                        const analysisResult = await analysisResponse.json();
    
                        // Obtener el nombre del archivo
                        const fileName = file.name;
    
                        // Redirigir a la página de resultados con los datos obtenidos
                        const params = new URLSearchParams({
                            detectionRatio: `${analysisResult.data.attributes.stats.malicious}/${analysisResult.data.attributes.stats.undetected + analysisResult.data.attributes.stats.malicious}`,
                            malicious: analysisResult.data.attributes.stats.malicious,
                            failure: analysisResult.data.attributes.stats.failure,
                            undetected: analysisResult.data.attributes.stats.undetected,
                            suspicious: analysisResult.data.attributes.stats.suspicious,
                            harmless: analysisResult.data.attributes.stats.harmless,
                            timeout: analysisResult.data.attributes.stats.timeout,
                            typeUnsupported: analysisResult.data.attributes.stats['type-unsupported'],
                            confirmedTimeout: analysisResult.data.attributes.stats['confirmed-timeout'],
                            fileName: fileName 
                            
                        });
    
                        window.location.href = `detalles/results.html?${params.toString()}`;
                    } catch (error) {
                        console.error('Error:', error);
                    }
                }
            });

//URL

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('urlForm');

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const urlInput = document.getElementById('urlInput').value;
        const apiUrl = 'http://localhost:9090/api/client';
        const data = { url: urlInput };
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };

        async function sendRequest() {
            try {
                const response = await fetch(apiUrl, options);
                if (!response.ok) {
                    throw new Error('Error en la solicitud: ' + response.statusText);
                }
                const responseData = await response.json();
                console.log('Respuesta de la API:', responseData);
                displayResults(responseData);
            } catch (error) {
                console.error('Error:', error);
                alert('Error al enviar la solicitud');
            }
        }

        // function displayResults(data) {
        //     const resultContainer = document.getElementById('resultContainer');
        //     resultContainer.innerHTML = `
        //         <div class="result-item">
        //             <span class="result-title">URL:</span> ${data.url}
        //         </div>
        //         <div class="result-item">
        //             <span class="result-title">Prediction:</span> ${data.prediction}
        //         </div>
        //         <div class="result-item">
        //             <span class="result-title">Probabilities:</span>
        //         </div>
        //         <div class="result-item">
        //             <div class="result-title">Benign: ${data.probabilities.benign}</div>
        //             <div class="probability-bar benign" style="width: ${parseFloat(data.probabilities.benign) * 100}%"></div>
        //         </div>
        //         <div class="result-item">
        //             <div class="result-title">Defacement: ${data.probabilities.defacement}</div>
        //             <div class="probability-bar defacement" style="width: ${parseFloat(data.probabilities.defacement) * 100}%"></div>
        //         </div>
        //         <div class="result-item">
        //             <div class="result-title">Malware: ${data.probabilities.malware}</div>
        //             <div class="probability-bar malware" style="width: ${parseFloat(data.probabilities.malware) * 100}%"></div>
        //         </div>
        //         <div class="result-item">
        //             <div class="result-title">Phishing: ${data.probabilities.phishing}</div>
        //             <div class="probability-bar phishing" style="width: ${parseFloat(data.probabilities.phishing) * 100}%"></div>
        //         </div>
        //     `;
        // }
        function displayResults(data) {
            const resultContainer = document.getElementById('resultContainer');
            resultContainer.innerHTML = `
                
                <div class="result-item">
                    <span class="result-title">Prediction:</span> ${data.prediction}
                </div>
                <div class="result-item">
                    <span class="result-title">Probabilities:</span>
                </div>
                <div class="result-item">
                    <div class="result-title">Benign: ${data.probabilities.benign}</div>
                    <div class="probability-bar benign" style="width: ${parseFloat(data.probabilities.benign) * 100}%"></div>
                </div>
                <div class="result-item">
                    <div class="result-title">Defacement: ${data.probabilities.defacement}</div>
                    <div class="probability-bar defacement" style="width: ${parseFloat(data.probabilities.defacement) * 100}%"></div>
                </div>
                <div class="result-item">
                    <div class="result-title">Malware: ${data.probabilities.malware}</div>
                    <div class="probability-bar malware" style="width: ${parseFloat(data.probabilities.malware) * 100}%"></div>
                </div>
                <div class="result-item">
                    <div class="result-title">Phishing: ${data.probabilities.phishing}</div>
                    <div class="probability-bar phishing" style="width: ${parseFloat(data.probabilities.phishing) * 100}%"></div>
                </div>
            `; 
            resultContainer.classList.remove('hidden');
          }
        sendRequest();
    });
});
// document.addEventListener('DOMContentLoaded', function() {
//     const form = document.getElementById('urlForm');

//     form.addEventListener('submit', function(event) {
//         event.preventDefault();

//         const urlInput = document.getElementById('urlInput').value;
//         const apiUrl = 'http://localhost:9090/api/client';
//         const data = { url: urlInput };
//         const options = {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(data)
//         };

//         async function sendRequest() {
//             try {
//                 const response = await fetch(apiUrl, options);
//                 if (!response.ok) {
//                     throw new Error('Error en la solicitud: ' + response.statusText);
//                 }
//                 const responseData = await response.json();
//                 console.log('Respuesta de la API:', responseData);
//                 displayResults(responseData);
//             } catch (error) {
//                 console.error('Error:', error);
//                 alert('Error al enviar la solicitud');
//             }
//         }

//         function displayResults(data) {
//             const resultContainer = document.getElementById('resultContainer');
//             resultContainer.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
//         }

//         sendRequest();
//     });
// });

        function showFileChooser() {
            document.getElementById('fileInputContainer').style.display = 'block';
            document.getElementById('urlInputContainer').style.display = 'none';
        }

        function showUrlInput() {
            document.getElementById('fileInputContainer').style.display = 'none';
            document.getElementById('urlInputContainer').style.display = 'block';
        }

        function redirectToPage() {
        window.location.href = 'usuario/data/dataset.html';
    }
