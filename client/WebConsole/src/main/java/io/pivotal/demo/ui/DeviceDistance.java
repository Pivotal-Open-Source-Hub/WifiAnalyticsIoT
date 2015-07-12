package io.pivotal.demo.ui;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceDistance {

	private String deviceId;
	
	private String piId;
	
	private double distance;
	
	
	public DeviceDistance(){};
	
	public DeviceDistance(String deviceId, String piId, double distance) {
		super();
		this.deviceId = deviceId;
		this.piId = piId;
		this.distance = distance;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getPiId() {
		return piId;
	}
	public void setPiId(String piId) {
		this.piId = piId;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	
	
}
