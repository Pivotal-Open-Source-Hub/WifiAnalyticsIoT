package io.pivotal.demo.ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.mortbay.util.ajax.JSON;

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

	private static String locatorHost = System.getProperty("locatorHost", "localhost");
	private static int locatorPort = Integer.getInteger("locatorPort", 10334);
	private static String username = "";
	private static String password = "";

    Region distances;
    Region pis;
    Region locations;

	static Logger logger = Logger.getLogger(GeodeClient.class.getCanonicalName());

    private GeodeClient() {
 
    	getCloudEnvProperties();
    	
		
		Properties props = new Properties();
		if (!username.isEmpty()){
			/*
			props.put("security-client-auth-init","templates.security.UserPasswordAuthInit.create");*/
			props.put("security-username", username);
			props.put("security-password", password);
		}
		
		logger.info(String.format("Geode Locator Information: %s[ %d ]",locatorHost, locatorPort));

        cache = new ClientCacheFactory(props)
								.addPoolLocator(locatorHost, locatorPort)
								.setPoolSubscriptionEnabled(true)
								.set("name", "GeodeClient")
								.set("cache-xml-file", "client.xml")
								.set("mcast-port", "0")
								.create();

        queryService = cache.getQueryService();
        distances = cache.getRegion(distancesRegionName);
        distances.registerInterest("ALL_KEYS");
        pis = cache.getRegion(PIsRegionName);
        locations = cache.getRegion(locationsRegionName);
        cache.getRegion("Probe_requests").registerInterest("ALL_KEYS");
    }
    
    /*
     * Parse the environment variables for services.
     */
    private void getCloudEnvProperties(){
    	String vcapServices = System.getenv("VCAP_SERVICES");
    	if (vcapServices==null || vcapServices.isEmpty()) return;
    	    	
		Object parsed = JSON.parse(vcapServices);
		Object[] gemServices = (Object[]) ((Map)parsed).get("p-gemfire");
		Map credentials=(Map)((Map)gemServices[0]).get("credentials");
		String locator = ((Object[])credentials.get("locators"))[0].toString();
		String user = credentials.get("username").toString();
		String pass = credentials.get("password").toString();
    	
		locatorHost = locator.substring(0, locator.indexOf("["));
		locatorPort = Integer.parseInt(locator.substring(locator.indexOf("[")+1, locator.indexOf("]")));
		this.username = user;
		this.password = pass;
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

	/*
	public static void main(String[] args){
		
		String prop = "{\"p-gemfire\":[{\"name\":\"gem-service\",\"label\":\"p-gemfire\",\"tags\":[\"gemfire\"],\"plan\":\"Default plan\",\"credentials\":{\"locators\":[\"10.68.43.55[55221]\",\"10.68.43.56[55221]\"],\"username\":\"ba223ab9-d4eb-46d6-544f-13868e761217\",\"password\":\"15068494869170888311\",\"rest_url\":\"\"}}]}";
		Object parsed = JSON.parse(prop);
		System.out.println(parsed);
		
		Object[] gemServices = (Object[]) ((Map)parsed).get("p-gemfire");
		Map credentials=(Map)((Map)gemServices[0]).get("credentials");
		String locator = ((Object[])credentials.get("locators"))[0].toString();
		String username = credentials.get("username").toString();
		String password = credentials.get("password").toString();
		System.out.println("Locators:" + locator);
		
		locatorHost = locator.substring(0, locator.indexOf("["));
		locatorPort = Integer.parseInt(locator.substring(locator.indexOf("[")+1, locator.indexOf("]")));
		
		System.out.println(locatorHost);
		System.out.println(locatorPort);
		
	}
	*/
	


}
