/**
 * 
 */
package nl.cyberworkz.roboflightmetrics.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import lombok.extern.slf4j.Slf4j;
import nl.cyberworkz.roboflightmetrics.handler.domain.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Interfaces with DestinationRepo.
 * 
 * @author haiko
 *
 */
@Slf4j
@Component
public class MetricsRepository {
	
	private static Logger LOG = LoggerFactory.getLogger(MetricsRepository.class);
	
	@Value("${metrics.tablename}")
	private String metricsTable;

	@Autowired
	private AmazonDynamoDB dynamoDBClient;

	public void addMetrics(Metrics metrics){
		DynamoDB db = new DynamoDB(dynamoDBClient);

		Table table =  db.getTable(metricsTable);
		Item item = new Item();
		item.withPrimaryKey("id", metrics.getOriginFlight().concat("-").concat(metrics.getClientIP()))
				.with("carrier", metrics.getCarrier())
				.with("clientIp", metrics.getClientIP())
				.with("originFlight", metrics.getOriginFlight())
				.with("eventTime", metrics.getEventTime())
				.with("landingTime", metrics.getLandingTime());

		PutItemOutcome outcome = table.putItem(item);
		log.info(outcome.toString());
	}

}
