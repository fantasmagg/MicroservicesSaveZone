package spring_boot_webflux_client_model.client.app.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stats {
	private int malicious;
    @JsonProperty("type-unsupported")
    private int typeUnsupported;
    private int failure;
    private int undetected;
    private int suspicious;
    @JsonProperty("confirmed-timeout")
    private int confirmedTimeout;
    private int harmless;
    private int timeout;

    // Getters y setters
    public int getMalicious() {
        return malicious;
    }

    public void setMalicious(int malicious) {
        this.malicious = malicious;
    }

    public int getTypeUnsupported() {
        return typeUnsupported;
    }

    public void setTypeUnsupported(int typeUnsupported) {
        this.typeUnsupported = typeUnsupported;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getUndetected() {
        return undetected;
    }

    public void setUndetected(int undetected) {
        this.undetected = undetected;
    }

    public int getSuspicious() {
        return suspicious;
    }

    public void setSuspicious(int suspicious) {
        this.suspicious = suspicious;
    }

    public int getConfirmedTimeout() {
        return confirmedTimeout;
    }

    public void setConfirmedTimeout(int confirmedTimeout) {
        this.confirmedTimeout = confirmedTimeout;
    }

    public int getHarmless() {
        return harmless;
    }

    public void setHarmless(int harmless) {
        this.harmless = harmless;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
     
}
