package io.pivotal.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.annotation.JsonProperty;

@ComponentScan
public class ProbeRequest {

	
	@JsonProperty("deviceId")
	private String device_id;
	
	@JsonProperty("signalDbm")
	private int signal_dbm;
	
	@JsonProperty("piId")
	private String piId;
	
	@JsonProperty("frequencyMhz")
	private int frequencyMhz;
	
	@JsonProperty("nanoTimestamp")
	private long nanoTimestamp;
	
	public ProbeRequest() {}
	
	public ProbeRequest(String device_id, int signal_dbm, int frequencyMhz, long timestamp) throws UnknownHostException {
		super();
		this.device_id = device_id;
		this.signal_dbm = signal_dbm;
		this.piId = InetAddress.getLocalHost().getHostName();
		this.frequencyMhz = frequencyMhz;
		this.nanoTimestamp = timestamp;
	}
	
	
	
	public ProbeRequest(String device_id, int signal_dbm, String piId,
			int frequencyMhz, long nanoTimestamp) {
		super();
		this.device_id = device_id;
		this.signal_dbm = signal_dbm;
		this.piId = piId;
		this.frequencyMhz = frequencyMhz;
		this.nanoTimestamp = nanoTimestamp;
	}

	public long getNanoTimestamp() {
		return nanoTimestamp;
	}

	public void setNanoTimestamp(long nanoTimestamp) {
		this.nanoTimestamp = nanoTimestamp;
	}

	public int getFrequencyMhz() {
		return frequencyMhz;
	}

	public void setFrequencyMhz(int frequencyMhz) {
		this.frequencyMhz = frequencyMhz;
	}

	
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public int getSignal_dbm() {
		return signal_dbm;
	}
	public void setSignal_dbm(int signal_dbm) {
		this.signal_dbm = signal_dbm;
	}
	
	public String getPiId() {
		return piId;
	}

	public void setPiId(String piId) {
		this.piId = piId;
	}


	
	
}
