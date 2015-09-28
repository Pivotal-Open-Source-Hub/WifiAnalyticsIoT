package io.pivotal.demo;

import io.pivotal.demo.simulator.DeviceSimulator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@Configuration
public class ProbeCaptureRunner implements CommandLineRunner {

	
	Logger logger = Logger.getLogger(ProbeCaptureRunner.class.getName());
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${serverUrl}")
    private String URL;
	
    @Value("${tshark_process}")
    private String tshark_command;
	
    @Value("${simulation}")
    private boolean simulation;
    
    @Autowired
    private DeviceSimulator simulator;

    
    public void run(String... args) throws Exception {
		
		if (simulation){
			
			simulator.run(args);
		}
		
		else{
	    	
			logger.info("--------------------------------------");
					
			logger.info("Capturing tshark process output...");
			
			Process tshark = Runtime.getRuntime().exec(tshark_command);
			try{
				if (!tshark.isAlive()){
					
					logger.severe("Process exited with code "+tshark.exitValue());	
					logger.severe(new BufferedReader(new InputStreamReader(tshark.getInputStream())).readLine());
					
					BufferedReader errorStream = new BufferedReader(new InputStreamReader(tshark.getErrorStream()));
					String errorLine = null;
					while ((errorLine = errorStream.readLine())!=null){
						logger.severe(errorLine);
					}						
				}
				
				BufferedReader br = new BufferedReader(new InputStreamReader(tshark.getInputStream()));
				while (tshark.isAlive()){
					String line = br.readLine();
					processLine(line);			
				}
				br.close();
				tshark.waitFor();
			}
			finally{
				if (tshark.isAlive()) tshark.destroyForcibly();
			}	
		}
	}
	protected void processLine(String line){
		StringTokenizer st = new StringTokenizer(line);
		
		if (st.countTokens()<3) return;
		String deviceId = st.nextToken();
		int signal_dbm = Integer.parseInt(st.nextToken());
		int frequency = Integer.parseInt(st.nextToken());
		
		try{
			long nanoTimestamp = System.nanoTime();
			ProbeRequest req = new ProbeRequest(deviceId,signal_dbm, frequency, nanoTimestamp);
			putProbeReq(req);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void putProbeReq(ProbeRequest req){
		ProbeRequest response = restTemplate.postForObject(URL, req, ProbeRequest.class);
	}
	

}
