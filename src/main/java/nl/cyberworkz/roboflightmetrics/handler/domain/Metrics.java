package nl.cyberworkz.roboflightmetrics.handler.domain;


import java.time.LocalDateTime;

public class Metrics {

    private String originFlight;

    private LocalDateTime landingTime;

    private String clientIP;

    private String carrier;

    private LocalDateTime eventTime;

    public String getOriginFlight() {
        return originFlight;
    }

    public void setOriginFlight(String originFlight) {
        this.originFlight = originFlight;
    }

    public LocalDateTime getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(LocalDateTime landingTime) {
        this.landingTime = landingTime;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
}
