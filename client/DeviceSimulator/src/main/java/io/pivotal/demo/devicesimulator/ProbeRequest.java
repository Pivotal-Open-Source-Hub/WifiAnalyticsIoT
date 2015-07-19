package io.pivotal.demo.devicesimulator;

import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.annotation.JsonProperty;

@ComponentScan
public class ProbeRequest {

	@JsonProperty("deviceId")
	private String deviceId;
	
	@JsonProperty("distance")
	private double distance;

	@JsonProperty("signal_dbm")	
	private double signalDbm;

	@JsonProperty("piId")	
	private double piId;

	@JsonProperty("nanoTimestamp")	
	private long timestamp;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String symbol) {
		this.deviceId = symbol;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double price) {
		this.distance = price;
	}

	public double getSignalDbm() {
		return signalDbm;
	}

	public void setSignalDbm(double high) {
		this.signalDbm = high;
	}

	public double getPiId() {
		return piId;
	}

	public void setPiId(double low) {
		this.piId = low;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	
}
