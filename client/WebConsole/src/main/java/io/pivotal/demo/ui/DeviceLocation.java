package io.pivotal.demo.ui;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceLocation{

	private double x;

	private double y;
	
	private String deviceId;
	
	
	public DeviceLocation(){}
	
	public DeviceLocation(String deviceId, double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.deviceId = deviceId;
	}
	
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	
}
