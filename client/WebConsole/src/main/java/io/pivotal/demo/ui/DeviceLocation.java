package io.pivotal.demo.ui;

import java.io.Serializable;

public class DeviceLocation implements Serializable, Comparable {

	private int x;
	private int y;
	private String deviceId;

	
	public DeviceLocation(){}
	
	public DeviceLocation(String deviceId, int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.deviceId = deviceId;
	}
	
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public int compareTo(Object o) {
		DeviceLocation l = (DeviceLocation)o;
		return this.deviceId.compareTo(l.deviceId);
	}
	
	
	
	
}
