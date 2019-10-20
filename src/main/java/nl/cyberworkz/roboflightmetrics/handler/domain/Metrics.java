package nl.cyberworkz.roboflightmetrics.handler.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Metrics {

    private String originFlight;

    private LocalDateTime landingTime;

    private String clientIP;

    private String carrier;

    private LocalDateTime eventTime;

}
