package nl.cyberworkz.roboflightmetrics.stack;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.events.targets.SqsQueue;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueProps;

public class RoboFlightMetricsStack extends Stack {

    RoboFlightMetricsStack(final Construct parent, final String name) {
        super(parent, name);

        // RoboFlightMonitor Lambda - existing resource in same account
        IFunction flightMonitorFn = Function.fromFunctionArn(this, "RoboFlightMonitorAPI", "arn:aws:lambda:eu-central-1:184690513648:function:awscodestar-roboflightmonit-l-RoboFlightMonitorAPI-AUN6CSV9YOJT");


        // SQS Queue
        Queue monitorEventQueue  = new Queue(this, "metrics-queue", QueueProps.builder()
                .withQueueName("metrics-queue")
                .build());

        //DynamoDB
        Table metricsTable = new Table(this, "metricsTable", TableProps.builder()
                .withTableName("metrics")
                .withReadCapacity(1)
                .withWriteCapacity(1)
                .withPartitionKey(Attribute.builder()
                        .withName("event_time")
                        .withType(AttributeType.STRING)
                        .build())
                .withSortKey(Attribute.builder()
                        .withName("origin_flight")
                        .withType(AttributeType.STRING)
                        .build())
                .build());

        // Lambda
        SingletonFunction metricsFunction = new SingletonFunction(this, "cdk-lambda-metrics",
                SingletonFunctionProps.builder()
                        .withDescription("Collection metrics from AWS SQS queue")
                        .withRuntime(Runtime.JAVA_8)
                        .withHandler("nl.cyberworkz.roboflightmetrics.handler.StreamLambdaHandler")
                        .withTimeout(Duration.seconds(60))
                        .withMemorySize(256).build());

        //grant sending messages and consuming messages to the queue .
        monitorEventQueue.grantSendMessages(flightMonitorFn);
        monitorEventQueue.grantConsumeMessages(metricsFunction);

        
    }
}
