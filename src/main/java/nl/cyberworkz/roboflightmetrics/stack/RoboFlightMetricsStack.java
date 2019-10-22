package nl.cyberworkz.roboflightmetrics.stack;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSourceProps;
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

    private final static String region = "eu-central-1";
    private final static  String account = "";

    RoboFlightMetricsStack(final Construct parent, final String name) {
        super(parent, name, StackProps.builder().
                env(Environment.builder()
                        .account(account)
                        .region(region)
                        .build())
                .build());

        // RoboFlightMonitor Lambda - existing resource in same account
        IFunction flightMonitorFn = Function.fromFunctionArn(this, "RoboFlightMonitorAPI", "arn:aws:lambda:eu-central-1:184690513648:function:awscodestar-roboflightmonit-l-RoboFlightMonitorAPI-AUN6CSV9YOJT");


        // SQS Queue
        Queue monitorEventQueue  = new Queue(this, "metrics-queue", QueueProps.builder()
                .queueName("metrics-queue")
                .visibilityTimeout(Duration.seconds(60)) // 6 times the duration of the timeout consuming Lambda function
                .build());

        //DynamoDB
        Table metricsTable = new Table(this, "metricsTable", TableProps.builder()
                .tableName("metrics")
                .readCapacity(1)
                .writeCapacity(1)
                .partitionKey(Attribute.builder()
                        .name("originFlight-clientIp")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("origin_flight")
                        .type(AttributeType.STRING)
                        .build())
                .build());

        // Lambda
        List<IEventSource> eventSources = new ArrayList<>();
        eventSources.add(new SqsEventSource(monitorEventQueue));

        SingletonFunction metricsFunction = new SingletonFunction(this, "cdk-lambda-metrics",
                SingletonFunctionProps.builder()
                        .uuid(UUID.randomUUID().toString())
                        .description("Collection metrics from AWS SQS queue")
                        .runtime(Runtime.JAVA_8)
                        .code(Code.asset("target/classes"))
                        .handler("nl.cyberworkz.roboflightmetrics.handler.MetricsConsumerLambdaHandler")
                        .timeout(Duration.seconds(10))
                        .memorySize(256)
                        .events(eventSources)
                        .tracing(Tracing.ACTIVE) // turn AWS XRay on
                        .build());

        //grant sending messages and consuming messages to the queue .
        monitorEventQueue.grantSendMessages(flightMonitorFn);
        monitorEventQueue.grantConsumeMessages(metricsFunction);
    }
}
