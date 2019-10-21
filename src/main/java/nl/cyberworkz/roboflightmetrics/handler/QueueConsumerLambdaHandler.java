/**
 * 
 */
package nl.cyberworkz.roboflightmetrics.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.cyberworkz.roboflightmetrics.handler.domain.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author haiko
 *
 */
@Component
public class QueueConsumerLambdaHandler implements RequestHandler<SQSEvent, Void> {

    private static Logger log = LoggerFactory.getLogger(QueueConsumerLambdaHandler.class);

    static {
        try {
            log.info("start lambda container init");



            // Retrieve dependent components from the application context
            appContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);

            log.info("endlambda container init");
        } catch (Exception e) {
            // if we fail here. We re-throw the exception to force another cold start
            log.error(e.getMessage(), e);
            throw new RuntimeException("Could not initialize Spring framework", e);
        }
    }

    private static ConfigurableApplicationContext appContext;


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MetricsRepository metricsRepository;

    public QueueConsumerLambdaHandler() {
        if(appContext == null){
            appContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        }

        mapper = appContext.getBeanFactory().getBean(ObjectMapper.class);
        metricsRepository = appContext.getBeanFactory().getBean(MetricsRepository.class);
    }


    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        try {
            for (SQSEvent.SQSMessage message : event.getRecords()) {
                log.info(message.getBody());

                Metrics metrics = mapper.readValue(message.getBody(), Metrics.class);
                metricsRepository.addMetrics(metrics);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

}

