package spring_boot_webflux_client_model.client.app.models;

import java.util.Map;

public class UrlDto {

    private String url;

    private String prediction;

    private Map<String, String> probabilities;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public Map<String, String> getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Map<String, String> probabilities) {
        this.probabilities = probabilities;
    }
}
