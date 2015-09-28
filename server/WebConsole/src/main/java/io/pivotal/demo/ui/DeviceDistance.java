package io.pivotal.demo.ui;


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


	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((piId == null) ? 0 : piId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceDistance other = (DeviceDistance) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (piId == null) {
			if (other.piId != null)
				return false;
		} else if (!piId.equals(other.piId))
			return false;
		return true;
	}
	
	
	
	
}
