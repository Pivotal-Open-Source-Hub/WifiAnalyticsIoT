package io.pivotal.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.logging.Logger;

//@ComponentScan
public class ProbeCaptureRunner{ // implements CommandLineRunner {

	private GeodeClient client = new GeodeClient();
	
	Logger logger = Logger.getLogger(ProbeCaptureRunner.class.getName());

	public void run(String... args) throws Exception {
		
				
		logger.info("--------------------------------------");

		Process tshark = Runtime.getRuntime().exec("tshark -i wlan1mon -I -f 'broadcast' -R 'wlan.fc.type == 0 && wlan.fc.subtype == 4' -T fields -e frame.time_epoch -e wlan.sa -e radiotap.dbm_antsignal");
		InputStream in = tshark.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = br.readLine())!=null && !line.isEmpty()){
			logger.info(line);
			StringTokenizer st = new StringTokenizer(line);
			long timestamp = Long.parseLong(st.nextToken());
			String deviceId = st.nextToken();
			int signal_dbm = Integer.parseInt(st.nextToken());
			
			ProbeRequest req = new ProbeRequest(timestamp,deviceId,signal_dbm);
			client.putProbeReq(req);
		}
		
		
		logger.info("done");
		
		
	}

}