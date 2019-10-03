package nl.cyberworkz.roboflightmetrics.handler.domain;

import java.time.LocalDateTime;

public class Metrics {

    private String originFlight;

    private LocalDateTime landingTime;

    private String carrier;

    private LocalDateTime eventTime;

    private Metrics(String originFlight, LocalDateTime landingTime, String carrier, LocalDateTime eventTime) {
        this.originFlight = originFlight;
        this.landingTime = landingTime;
        this.carrier = carrier;
        this.eventTime = eventTime;
    }

    public static Metrics createMetric(String originFlight, LocalDateTime landingTime, String carrier, LocalDateTime eventTime) {
        return new Metrics(originFlight, landingTime, carrier, eventTime);
    }

}
