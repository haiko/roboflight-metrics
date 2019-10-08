package nl.cyberworkz.roboflightmetrics.stack;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueProps;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  Stack for a Lambda reading from a SQS queue and writing to a Dynamo Table.
 *
 *  References:
 *
 * @see https://docs.aws.amazon.com/lambda/latest/dg/with-sqs.html - SQS with Lambda
 *
 *
 *
 *
 */
public class RoboFlightMetricsStack extends Stack {

    private final static String region = "";
    private final static  String account = "";

    RoboFlightMetricsStack(final Construct parent, final String name) {
        super(parent, name, StackProps.builder()
                .withEnv(Environment.builder()
                        .withAccount(account)
                        .withRegion(region)
                        .build())
                .build());

        // RoboFlightMonitor Lambda - existing resource in same account
        IFunction flightMonitorFn = Function.fromFunctionArn(this, "RoboFlightMonitorAPI", "arn:aws:lambda:eu-central-1:184690513648:function:awscodestar-roboflightmonit-l-RoboFlightMonitorAPI-AUN6CSV9YOJT");


        // SQS Queue
        Queue monitorEventQueue  = new Queue(this, "metrics-queue", QueueProps.builder()
                .withQueueName("metrics-queue")
                .withVisibilityTimeout(Duration.seconds(60)) // 6 times the duration of the timeout consuming Lambda function
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
        List<IEventSource> eventSources = new ArrayList<>();
        eventSources.add(new SqsEvent)

        SingletonFunction metricsFunction = new SingletonFunction(this, "cdk-lambda-metrics",
                SingletonFunctionProps.builder()
                        .withUuid(UUID.randomUUID().toString())
                        .withDescription("Collection metrics from AWS SQS queue")
                        .withRuntime(Runtime.JAVA_8)
                        .withCode(Code.asset("target/classes"))
                        .withHandler("nl.cyberworkz.roboflightmetrics.handler.StreamLambdaHandler")
                        .withTimeout(Duration.seconds(10))
                        .withMemorySize(256)
                        .withEvents()
                        .build());

        //grant sending messages and consuming messages to the queue .
        monitorEventQueue.grantSendMessages(flightMonitorFn);
        monitorEventQueue.grantConsumeMessages(metricsFunction);
    }
}
