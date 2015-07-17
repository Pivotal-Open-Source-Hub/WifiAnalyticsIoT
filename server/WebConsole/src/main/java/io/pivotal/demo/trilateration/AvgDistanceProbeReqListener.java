package io.pivotal.demo.trilateration;

import io.pivotal.demo.ui.DeviceDistance;
import io.pivotal.demo.ui.GeodeClient;

import java.util.Properties;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import com.gemstone.gemfire.pdx.PdxInstance;

public class AvgDistanceProbeReqListener extends CacheListenerAdapter implements Declarable {

	private static final int NUMBER_OF_AVG_PERIODS = 10;
	
	
	@Override
	public void afterCreate(EntryEvent event) {
		super.afterCreate(event);
		updateAverageDistance(event);
	}

	@Override
	public void afterUpdate(EntryEvent event) {		
		super.afterUpdate(event);
		updateAverageDistance(event);
	}

	private void updateAverageDistance(EntryEvent e){
		
		Object obj = e.getNewValue();
		String piId = null;
		String deviceId = null;
		double distance = 0;
		if (obj instanceof PdxInstance){			
			piId = (String)((PdxInstance)obj).getField("piId");
			deviceId = (String)((PdxInstance)obj).getField("deviceId");
			distance = (double)((PdxInstance)obj).getField("distance");
		}
		else throw new RuntimeException("new object is not PDX Instance.. it came as "+obj.getClass());
		
		double[] distances = GeodeClient.getInstance().getLatestDistanceMeasurements(piId, deviceId, NUMBER_OF_AVG_PERIODS);
		
		double sumOfDistances = distance;
		for (int i=0; i<distances.length; i++){
			sumOfDistances += distances[i];
		}
		
		GeodeClient.getInstance().setDeviceDistance(new DeviceDistance(deviceId, piId, sumOfDistances / distances.length));
	}

	@Override
	public void init(Properties arg0) {
	}
	
	
	
}
