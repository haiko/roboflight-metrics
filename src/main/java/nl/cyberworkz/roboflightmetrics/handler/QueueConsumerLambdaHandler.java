/**
 * 
 */
package nl.cyberworkz.roboflightmetrics.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.cyberworkz.roboflightmetrics.handler.domain.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author haiko
 *
 */
@Slf4j
@Component
public class QueueConsumerLambdaHandler implements RequestHandler<SQSEvent, Void> {

    static {
        try {
            log.info("start lambda container init");

            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(QueueConsumerLambdaHandler.class, MetricsRepository.class, ApplicationConfig.class);

            log.info("endlambda container init");
        } catch (Exception e) {
            // if we fail here. We re-throw the exception to force another cold start
            log.error(e.getMessage(), e);
            throw new RuntimeException("Could not initialize Spring framework", e);
        }
    }

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        try {
            for (SQSEvent.SQSMessage message : event.getRecords()) {
                String input = message.getBody();

                Metrics metrics = mapper.readValue(message.getBody(), Metrics.class);

            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

}

