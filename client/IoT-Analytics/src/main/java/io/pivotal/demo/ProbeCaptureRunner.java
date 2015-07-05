package io.pivotal.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

//@ComponentScan
public class ProbeCaptureRunner{ // implements CommandLineRunner {

	private GeodeClient client = new GeodeClient();
	
	Logger logger = Logger.getLogger(ProbeCaptureRunner.class.getName());

	public void run(String... args) throws Exception {
		
				
		logger.info("--------------------------------------");
				
		logger.info("Capturing tshark process output...");
		
	 	//Process tshark = Runtime.getRuntime().exec("sudo tshark -i wlan1mon -I -l -f broadcast -R wlan.fc.subtype==4 -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal -e radiotap.channel.freq");
		Process tshark = Runtime.getRuntime().exec("/usr/local/bin/tshark -i en0 -I -l -f broadcast -Y wlan.fc.subtype==4 -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal -e radiotap.channel.freq");
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
	protected void processLine(String line){
		StringTokenizer st = new StringTokenizer(line);
		String timeepoch = st.nextToken();
		String deviceId = st.nextToken();
		int signal_dbm = Integer.parseInt(st.nextToken());
		int frequency = Integer.parseInt(st.nextToken());
		
		try{
			long nanoTimestamp = System.nanoTime();
			ProbeRequest req = new ProbeRequest(timeepoch,deviceId,signal_dbm, frequency, nanoTimestamp);
			client.putProbeReq(req);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception{
		ProbeCaptureRunner runner = new ProbeCaptureRunner();
		Iterator<Object> probes = runner.client.getAll().iterator();
		while (probes.hasNext()){
			ProbeRequest req = (ProbeRequest)probes.next();
			if (req.getHostname()==null || req.getHostname().isEmpty())
				req.setHostname(InetAddress.getLocalHost().getHostAddress());
			if (req.getFrequencyMhz()==0)
				req.setFrequencyMhz(2412);
			runner.client.putProbeReq(req);
			
		}
	}

}
