package iot.pivotal.demo.particlefilter;

import io.pivotal.demo.trilateration.CalculateLocationDistanceListener;
import io.pivotal.demo.ui.DeviceLocation;
import io.pivotal.demo.ui.GeodeClient;

import java.util.Iterator;

import org.junit.Test;

public class ParticleFIlterTest {


	//@Test
	public void testParticleFilter(){

		// Obtain a deviceId
		
		Iterator<DeviceLocation> deviceLocations = GeodeClient.getInstance().getDeviceLocations().iterator();
		if (!deviceLocations.hasNext()) throw new RuntimeException("No device found");
		
		String deviceId = deviceLocations.next().getDeviceId();
		
		CalculateLocationDistanceListener.updateDeviceLocation(deviceId);
	
	}
	
}
