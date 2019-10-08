/**
 * 
 */
package nl.cyberworkz.roboflightmetrics.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import nl.cyberworkz.roboflightmetrics.handler.domain.Metrics;

/**
 * @author haiko
 *
 */
public class QueueConsumerLambdaHandler implements RequestHandler<SQSEvent, Void> {


    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        try {
            for (SQSEvent.SQSMessage message : event.getRecords()) {
                Metrics metrics = null;
                String input = message.getBody();

            }
        } catch (Exception ex) {
            throw ex;
        }
        return null;
    }

}

