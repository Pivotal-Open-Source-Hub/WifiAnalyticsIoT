package io.pivotal.demo.ui;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class DeviceMap implements Serializable{

	public Set<DeviceLocation> devices = new TreeSet<DeviceLocation>();

	public Set<DeviceLocation> getDevices() {
		return devices;
	}

	public void setDevices(Set<DeviceLocation> devices) {
		this.devices = devices;
	}
	
	
	public void addDevice(String deviceId, int x, int y){
		devices.add(new DeviceLocation(deviceId, x, y));
	}
	
}
