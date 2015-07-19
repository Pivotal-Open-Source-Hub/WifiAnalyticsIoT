package io.pivotal.demo.simulator;

import io.pivotal.demo.ProbeRequest;

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

    @Value("${maxNumberOfSamples}")
	private int maxNumberOfSamples;

    
    private RestTemplate restTemplate = new RestTemplate();
	
	Logger logger = Logger.getLogger(DeviceSimulator.class.getName());

	@Override
	public void run(String... args) throws Exception {
		
		logger.info(">>>>> RUNNING SIMULATION MODE");		
		logger.info("--------------------------------------");
		logger.info(">>> Geode rest endpoint: "+geodeURL);
		logger.info(">>> Endpoint URL: "+URL);
		logger.info("--------------------------------------");
		
		List objects = restTemplate.getForObject(geodeURL+"/gemfire-api/v1/queries/adhoc?q=SELECT%20DISTINCT%20*%20FROM%20/Device_simulation%20s%20ORDER%20BY%20nanoTimestamp%20desc%20limit%20"+maxNumberOfSamples, List.class);

		logger.info(">>> Posting "+objects.size()+" messages ...");

		for (int i=0; i<objects.size(); i++){
			Map<String,Object> map = (Map)objects.get(i);
			ProbeRequest request = new ProbeRequest();
			request.setDevice_id((String)map.get("deviceId"));
			request.setPiId((String)(map.get("piId").toString()));
			request.setSignal_dbm((int)(map.get("signalDbm")));
			request.setNanoTimestamp((long)map.get("nanoTimestamp"));
			request.setFrequencyMhz((int)map.get("frequencyMhz"));			
			ProbeRequest response = restTemplate.postForObject(URL, request, ProbeRequest.class);
			Thread.sleep(delay);
		}
		
		
		
		logger.info("done");
		
		
	}
	
	/*
	public static void main(String[] args){
		SpringApplication.run(DeviceSimulator.class, args);
	}*/

}