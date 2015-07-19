package io.pivotal.demo.devicesimulator;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@Configuration
public class DeviceSimulator implements CommandLineRunner {

	@Value("${serverUrl}") 
	private String URL;
	
	@Value("${geodeUrl}") 
	private String geodeURL;
	
	@Value("${delayInMs}") 
	private long delay;

	private RestTemplate restTemplate = new RestTemplate();
	
	Logger logger = Logger.getLogger(DeviceSimulator.class.getName());

	@Override
	public void run(String... args) throws Exception {
		
				
		logger.info("--------------------------------------");
		logger.info(">>> Geode rest endpoint: "+geodeURL);
		logger.info(">>> Endpoint URL: "+URL);
		logger.info("--------------------------------------");
		
		List objects = restTemplate.getForObject(geodeURL+"/gemfire-api/v1/queries/adhoc?q=SELECT%20DISTINCT%20*%20FROM%20/Probe_requests%20s%20ORDER%20BY%22nanoTimestamp%22%", List.class);

		logger.info(">>> Posting "+objects.size()+" messages ...");

		for (int i=0; i<objects.size(); i++){
			Map<String,Object> map = (Map)objects.get(i);
			ProbeRequest price = new ProbeRequest();
			price.setDeviceId((String)map.get("deviceId"));
			price.setPiId(new Double(map.get("piId").toString()));
			price.setSignalDbm(new Double(map.get("signal_dbm").toString()));
			price.setTimestamp((Long)map.get("nanoTimestamp"));
			ProbeRequest response = restTemplate.postForObject(URL, price, ProbeRequest.class);
			Thread.sleep(delay);
		}
		
		
		
		logger.info("done");
		
		
	}

}