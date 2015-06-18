package io.pivotal.demo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProbeRequest implements Serializable {

	private long probe_timestamp;
	private String device_id;
	private int signal_dbm;
	

	
	public ProbeRequest(long probe_timestamp, String device_id, int signal_dbm) {
		super();
		this.probe_timestamp = probe_timestamp;
		this.device_id = device_id;
		this.signal_dbm = signal_dbm;
	}
	
	public long getProbe_timestamp() {
		return probe_timestamp;
	}
	public void setProbe_timestamp(long probe_timestamp) {
		this.probe_timestamp = probe_timestamp;
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
	
	
	
	
}
