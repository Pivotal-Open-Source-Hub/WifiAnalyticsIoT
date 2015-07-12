package iot.pivotal.demo.trilateration;

import io.pivotal.demo.ui.DeviceLocation;
import io.pivotal.demo.ui.GeodeClient;

import java.net.UnknownHostException;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class CreatePIsTest {

	
	@Test
	public void testCreation(){
		
		
		DeviceLocation pi1 = new DeviceLocation("Pi-1", 0,0);
		DeviceLocation pi2 = new DeviceLocation("Pi-2", 600,0);
		DeviceLocation pi3 = new DeviceLocation("Pi-3", 300,500);
		
		GeodeClient.getInstance().setPILocation(pi1);
		GeodeClient.getInstance().setPILocation(pi2);
		GeodeClient.getInstance().setPILocation(pi3);
		
	}		
	
}
