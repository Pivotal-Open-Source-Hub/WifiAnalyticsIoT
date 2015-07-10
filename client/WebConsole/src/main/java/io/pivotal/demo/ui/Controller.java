package io.pivotal.demo.ui;


import io.pivotal.demo.trilateration.CalculateLocationDistanceListener;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/controller")
public class Controller {
    
    public Controller() {
        
    }
    
 
    @RequestMapping(value="/getDeviceMap")
    public @ResponseBody DeviceMap getDeviceMap(){
    	
    	Collection<DeviceLocation> deviceLocations = GeodeClient.getInstance().getDeviceLocations();
    	DeviceMap map = new DeviceMap();
        
    	Iterator<DeviceLocation> it = deviceLocations.iterator();
    	while (it.hasNext()){
    		DeviceLocation device = it.next();
    		map.addDevice(device.getDeviceId(), device.getX(), device.getY());
    	}
    	
        return map;

    }    
    
    @RequestMapping(value="/getPIs")
    public @ResponseBody DeviceMap getPIs(){
    	
    	Collection<DeviceLocation> piLocations = GeodeClient.getInstance().getPIsLocation();
    	DeviceMap map = new DeviceMap();
        
    	Iterator<DeviceLocation> it = piLocations.iterator();
    	while (it.hasNext()){
    		DeviceLocation pi = it.next();
    		map.addDevice(pi.getDeviceId(), pi.getX(), pi.getY());
    	}
    	
        return map;
    }        
    
    @RequestMapping(value="/updatePILocation")
    public void updatePILocation(@RequestParam("deviceId") String deviceId, @RequestParam("x") double x, @RequestParam("y") double y){
    	
    	DeviceLocation piLocation = new DeviceLocation(deviceId, x, y);
    	GeodeClient.getInstance().setPILocation(piLocation);
    	recalculateAllPositions();
    }   
    
    private void recalculateAllPositions(){
    	
    	Iterator<DeviceLocation> devices = GeodeClient.getInstance().getDeviceLocations().iterator();
    	while (devices.hasNext()){
    		DeviceLocation device = devices.next();
    		CalculateLocationDistanceListener.updateDeviceLocation(device.getDeviceId());
    	}
    	
    }
    
    
}