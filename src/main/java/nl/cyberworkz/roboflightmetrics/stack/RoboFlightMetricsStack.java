package nl.cyberworkz.roboflightmetrics.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.SingletonFunction;
import software.amazon.awscdk.services.lambda.SingletonFunctionProps;

public class RoboFlightMetricsStack extends Stack {

    RoboFlightMetricsStack(final Construct parent, final String name) {
        super(parent, name);

        SingletonFunction metricsFunction = new SingletonFunction(this, "cdk-lambda-metrics",
                SingletonFunctionProps.builder()
                        .withDescription("Collection metrics from AWS SQS queue")
                        .withRuntime(Runtime.JAVA_8)
                        .withHandler("nl.cyberworkz.roboflightmetrics.handler.StreamLambdaHandler")
                        .withTimeout(Duration.seconds(60))
                        .withMemorySize(256).build());

    }
}
