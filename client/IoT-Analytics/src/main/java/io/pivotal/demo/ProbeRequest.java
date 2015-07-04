package io.pivotal.demo;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ProbeRequest {

	private String probe_timeepoch;
	private String device_id;
	private int signal_dbm;
	private String hostname;
	
	public ProbeRequest() {}
	
	public ProbeRequest(String probe_timeepoch, String device_id, int signal_dbm) throws UnknownHostException {
		super();
		this.probe_timeepoch = probe_timeepoch;
		this.device_id = device_id;
		this.signal_dbm = signal_dbm;
		this.hostname = InetAddress.getLocalHost().getHostName();
	}
	
	public String getProbe_timeepoch() {
		return probe_timeepoch;
	}
	public void setProbe_timeepoch(String probe_timeepoch) {
		this.probe_timeepoch = probe_timeepoch;
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
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}


	
	
}
