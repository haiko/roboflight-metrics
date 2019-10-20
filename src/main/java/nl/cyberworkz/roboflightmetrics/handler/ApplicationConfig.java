package nl.cyberworkz.roboflightmetrics.handler;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.handlers.TracingHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

	private static final long MAX_UPLOAD_SIZE = 125_829_120L;

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        return configurer;
    }
    
    @Bean
    public AmazonDynamoDB createDynamoDBClient() {
    	AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClient.builder();
    	builder.setRegion(com.amazonaws.regions.Regions.EU_CENTRAL_1.getName());
    	
    	ClientConfiguration clientConfiguration = new ClientConfiguration();
    	// timeout of 5 seconds
    	clientConfiguration.setClientExecutionTimeout(5*1000);
    	builder.setClientConfiguration(clientConfiguration);
    	builder.withRequestHandlers(new TracingHandler(AWSXRay.getGlobalRecorder()));
    	return builder.build();
    }
}
