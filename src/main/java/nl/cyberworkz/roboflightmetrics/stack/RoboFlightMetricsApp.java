package nl.cyberworkz.roboflightmetrics.stack;

import software.amazon.awscdk.core.App;

public class RoboFlightMetricsApp {

    public static void main(final String[] args) {
        App app = new App();

        new RoboFlightMetricsStack(app, "cdk-metrics");

        app.synth();
    }
}
