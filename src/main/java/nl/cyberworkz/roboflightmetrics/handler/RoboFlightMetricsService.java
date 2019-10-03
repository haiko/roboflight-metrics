/**
 * 
 */
package nl.cyberworkz.roboflightmetrics.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

/**
 * Handles business logic for metrics.
 * 
 * @author haiko
 *
 */
@Service
@Import(MetricsRepository.class)
public class RoboFlightMetricsService {

	private static final Logger LOG = LoggerFactory.getLogger(RoboFlightMetricsService.class);



	@Autowired
	private ObjectMapper mapper;

	private TimeZone tz;

	private DateTimeZone dtz;

	public RoboFlightMetricsService() {
		tz = TimeZone.getTimeZone("Europe/Amsterdam");
		dtz = DateTimeZone.forTimeZone(tz);
	}



}
