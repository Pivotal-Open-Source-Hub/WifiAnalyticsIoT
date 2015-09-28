package io.pivotal.demo.trilateration;

import io.pivotal.demo.ui.DeviceDistance;
import io.pivotal.demo.ui.DeviceLocation;
import io.pivotal.demo.ui.GeodeClient;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import com.gemstone.gemfire.pdx.PdxInstance;
import com.pivotal.demo.particlefilter.ParticleFilter;

public class CalculateLocationDistanceListener extends
		CacheListenerAdapter implements Declarable {

	
    private static final boolean useParticleFilter = true;
	
	
	public CalculateLocationDistanceListener() {
	}
	
	@Override
	public void init(Properties arg0) {
	}

	@Override
	public void afterCreate(EntryEvent event) {
		registerPI(event);
		handleUpdates(event);
	}
	
	

	@Override
	public void afterUpdate(EntryEvent event) {
		registerPI(event);		
		handleUpdates(event);
	}

	private void registerPI(EntryEvent event) {

		Object obj = event.getNewValue();
		String piId = null;
		if (obj instanceof PdxInstance){			
			piId = (String)((PdxInstance)obj).getField("piId");
		}
		else{
			piId = ((DeviceDistance)obj).getPiId();
		}
		
		if (GeodeClient.getInstance().getPILocation(piId)==null){
			GeodeClient.getInstance().setPILocation(new DeviceLocation(piId, 0, 0));
		}
		
	}

	private void handleUpdates(EntryEvent event){
		
		Object obj = event.getNewValue();
		if (obj instanceof PdxInstance){
			updateDeviceLocation((String)((PdxInstance)obj).getField("deviceId"));
		}
		else{
			updateDeviceLocation(((DeviceDistance)obj).getDeviceId());
		}
		
		
	};
	
	
	


	public static void updateDeviceLocation(String deviceId) {
		
		if (!useParticleFilter){
			// Standard Trilateration
	    	Map<DeviceDistance,DeviceLocation> devicesDistances = getScaledDeviceDistancePerPI(deviceId);
	    	if (devicesDistances==null || devicesDistances.size()==0) return;
	    	
			Iterator<DeviceDistance> keys = devicesDistances.keySet().iterator();
			double[][] positions = new double[devicesDistances.size()][devicesDistances.size()];
			double[] distances = new double[devicesDistances.size()];
			
			int index=0;
			while (keys.hasNext()){			
				DeviceDistance distance = keys.next();
				distances[index]=distance.getDistance();
				DeviceLocation piLocation = devicesDistances.get(distance);
				positions[index]=new double[]{piLocation.getX(), piLocation.getY()};
				index++;
			}
			
			double[] location = Trilateration.calculate(positions, distances);
			DeviceLocation deviceLocation = new DeviceLocation();
			deviceLocation.setDeviceId(deviceId);
			deviceLocation.setX(location[0]);
			deviceLocation.setY(location[1]);
		}
		else{
			// Particle Filter
			Map<DeviceDistance,DeviceLocation> deviceDistancePerPI = getScaledDeviceDistancePerPI(deviceId);
			ParticleFilter particleFilter = new ParticleFilter(deviceId, deviceDistancePerPI);
	    	DeviceLocation deviceLocation = particleFilter.calculate();		
			GeodeClient.getInstance().updateDeviceLocation(deviceLocation);
		}
	}

	public static Map<DeviceDistance,DeviceLocation> getScaledDeviceDistancePerPI(String deviceId){
	
		Map<DeviceDistance,DeviceLocation> deviceDistancePerPI = GeodeClient.getInstance().getDeviceDistancePerPI(deviceId);
		
		Iterator<DeviceDistance> distancesIt = deviceDistancePerPI.keySet().iterator();
		while (distancesIt.hasNext()){
			
			DeviceDistance distance = distancesIt.next();
			distance.setDistance(distance.getDistance() * Scale.getInstance().getScaleFactor());
			
		}
		return deviceDistancePerPI;
		
	}
	
}
