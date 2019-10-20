package nl.cyberworkz.roboflightmetrics.handler.domain;

import java.time.LocalDateTime;

public class Metrics {

    private String originFlight;

    private LocalDateTime landingTime;

    private String clientIP;

    private String carrier;

    private LocalDateTime eventTime;

    private Metrics(String originFlight, LocalDateTime landingTime, String carrier, LocalDateTime eventTime, String clientIP) {
        this.originFlight = originFlight;
        this.landingTime = landingTime;
        this.carrier = carrier;
        this.eventTime = eventTime;
        this.clientIP = clientIP;
    }

    public static Metrics createMetric(String originFlight, LocalDateTime landingTime, String carrier, LocalDateTime eventTime, String clientIP) {
        return new Metrics(originFlight, landingTime, carrier, eventTime, clientIP);
    }

}
