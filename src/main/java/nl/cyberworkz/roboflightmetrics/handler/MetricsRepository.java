/**
 * 
 */
package nl.cyberworkz.roboflightmetrics.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Interfaces with DestinationRepo.
 * 
 * @author haiko
 *
 */
@Service
public class MetricsRepository {
	
	private static Logger LOG = LoggerFactory.getLogger(MetricsRepository.class);
	
	@Value("${metrics.tablename}")
	private String metricsTable;

	@Autowired
	private AmazonDynamoDB dynamoDBClient;	
	

}
