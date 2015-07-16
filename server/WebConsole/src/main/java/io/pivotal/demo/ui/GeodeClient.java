package io.pivotal.demo.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.query.Query;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.cache.query.Struct;
import com.gemstone.gemfire.pdx.PdxInstance;

public class GeodeClient {

    private  ClientCache cache;

    private QueryService queryService;
    private static String distancesRegionName = "Distances";
    private static String PIsRegionName = "RaspberryPIs";
    private static String locationsRegionName = "DeviceLocations";
    
    private static GeodeClient instance;
    
    Region distances;
    Region pis;
    Region locations;

    private GeodeClient() {    	
    	
        cache = new ClientCacheFactory()
    		.set("name", "GemFireClient")
    		.set("cache-xml-file", "client.xml")
    		.create();
    	
        queryService = cache.getQueryService();
        distances = cache.getRegion(distancesRegionName);
        distances.registerInterest("ALL_KEYS");
        pis = cache.getRegion(PIsRegionName);
        locations = cache.getRegion(locationsRegionName);
        cache.getRegion("Probe_requests").registerInterest("ALL_KEYS");
    }
    
    
    public static synchronized GeodeClient getInstance(){
    	if (instance==null) instance = new GeodeClient();
    	return instance;
    }

	public Collection<DeviceLocation> getPIsLocation() {
    	Query query = queryService.newQuery("select distinct * from /RaspberryPIs r order by r.deviceId");
    	try {
			Collection<DeviceLocation> distances = (Collection<DeviceLocation>)query.execute();
			return distances;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public DeviceLocation getPILocation(String piId) {
    	return (DeviceLocation)pis.get(piId);
	}	
    
	public Map<DeviceDistance,DeviceLocation> getDeviceDistancePerPI(String deviceId){
		
		Map<DeviceDistance,DeviceLocation> deviceDistancePerPI = new HashMap<DeviceDistance, DeviceLocation>();
		
	   	Query query = queryService.newQuery("select d.deviceId, d.distance, d.piId, pi.x, pi.y from /Distances d, /RaspberryPIs pi where pi.deviceId=d.piId and d.deviceId=$1");
	   	try {
	   		Iterator results = ((Collection)query.execute(new Object[]{deviceId})).iterator();
	   		
	   		Map<DeviceDistance,DeviceLocation> resultMap = new HashMap<DeviceDistance, DeviceLocation>();
	   		
	   		while (results.hasNext()){
	   			Struct result = (Struct) results.next();
	   			
	   			double distance  = (Double)result.getFieldValues()[1] * 2;// * ((Scale.getX() + Scale.getY())/2);
	   			String piId = (String)result.getFieldValues()[2];
	   			double piX = (Double)result.getFieldValues()[3]; 
	   			double piY = (Double)result.getFieldValues()[4];
	   			
	   			DeviceDistance devDistance = new DeviceDistance(deviceId, piId, distance);
	   			DeviceLocation piLocation = new DeviceLocation(piId, piX, piY);
	   			deviceDistancePerPI.put(devDistance, piLocation); 
	   			
	   		}
	   		return deviceDistancePerPI;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		
	}
	
	public void setDeviceDistance(DeviceDistance distance){
		distances.put(distance.getDeviceId()+distance.getPiId(), distance);
	}
	
	
	public void setPILocation(DeviceLocation pi) {
		pis.put(pi.getDeviceId(), pi);
	}


	public void updateDeviceLocation(DeviceLocation deviceLocation) {
		locations.put(deviceLocation.getDeviceId(), deviceLocation);
		
	}
    
	public Collection<DeviceLocation> getDeviceLocations() {
		Query query = queryService.newQuery("select * from /"+locationsRegionName);
	   	try {
	   		return (Collection<DeviceLocation>)query.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	public double[] getLatestDistanceMeasurements(String piId, String deviceId, int numberOfResults){
	
	   	Query query = queryService.newQuery("select distinct * from /Probe_requests p where p.piId=$1 and p.deviceId=$2 order by p.nanoTimestamp desc limit "+numberOfResults);
	   	try {
	   		Collection results = (Collection)query.execute(new Object[]{piId, deviceId});
	   		double[] distances = new double[results.size()];
	   		
	   		Iterator resultsIt = results.iterator();
	   		
	   		int i=0;
	   		while (resultsIt.hasNext()){
	   			PdxInstance result = (PdxInstance) resultsIt.next();	   			
	   			double distance  = (double)result.getField("distance");
	   			distances[i] = distance;
	   			i++;
	   		}
	   		return distances;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		
		
		
	}



}
