package io.pivotal.demo.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeviceMap implements Serializable{

	public List<DeviceLocation> devices = new ArrayList<DeviceLocation>();

	public Collection<DeviceLocation> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceLocation> devices) {
		this.devices = devices;
	}
	
	
	public void addDevice(String deviceId, double x, double y){
		devices.add(new DeviceLocation(deviceId, x, y));
	}
	
}
