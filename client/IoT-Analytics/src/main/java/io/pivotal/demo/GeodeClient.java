package io.pivotal.demo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;

//@ComponentScan
public class GeodeClient {

	private static final String PROBE_REGION = "Probe_requests";
	
	private ClientCacheFactory clientCacheFactory;
	ClientCache cache;
	private Region<Long, ProbeRequest> probesRegion;
	
	
	public GeodeClient(){
		
		clientCacheFactory = 
				new ClientCacheFactory()
					.set("archive-file-size-limit", "100")
					.set("statistic-sampling-enabled", "true")
					.set("cache-xml-file", "cache-client.xml");
		
		cache = clientCacheFactory.create();
		probesRegion = cache.getRegion(PROBE_REGION);
		
	}
	
	public void putProbeReq(ProbeRequest req){
		probesRegion.put(req.getNanoTimestamp(), req);
	}
	
	public void removeProbeReq(Object key){
		probesRegion.destroy(key);
	}
	
	
	public List<Object> getAll() throws Exception{
		return probesRegion.query("Select * from /Probe_requests").asList();
	}
}
