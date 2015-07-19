package io.pivotal.demo.devicesimulator;

import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.annotation.JsonProperty;

@ComponentScan
public class ProbeRequest {

	@JsonProperty("deviceId")
	private String deviceId;
	
	@JsonProperty("distance")
	private double distance;

	@JsonProperty("signalDbm")	
	private double signalDbm;

	@JsonProperty("piId")	
	private String piId;

	@JsonProperty("nanoTimestamp")	
	private long timestamp;
	
	@JsonProperty("frequencyMhz")
	private int frequencyMhz;

	public int getFrequencyMhz() {
		return frequencyMhz;
	}

	public void setFrequencyMhz(int frequencyMhz) {
		this.frequencyMhz = frequencyMhz;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getSignalDbm() {
		return signalDbm;
	}

	public void setSignalDbm(double signal) {
		this.signalDbm = signal;
	}

	public String getPiId() {
		return piId;
	}

	public void setPiId(String id) {
		this.piId = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	
}
