package io.pivotal.demo.trilateration;


public class Scale {

	private double maxX=800;
	private double maxY=800;
	
	private double scaledXinMeters=3;
	private double scaledYinMeters=3;
	
	
	private static Scale instance = null;
	
	
	public static synchronized Scale getInstance(){
		if (instance==null) instance = new Scale();
		return instance;
	}
	
	
	public double getScaleFactor(){
		
		return Math.sqrt(((maxX / scaledXinMeters) * (maxY / scaledYinMeters))) ;
		
		
	}

	public void setDimensions(double maxX, double maxY, double metersX, double metersY){
		this.setMaxX(maxX);
		this.setMaxY(maxY);
		this.setScaledXinMeters(metersX);
		this.setScaledYinMeters(metersY);
	}
	
	public double getMaxX() {
		return maxX;
	}


	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}


	public double getMaxY() {
		return maxY;
	}


	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}


	public double getScaledXinMeters() {
		return scaledXinMeters;
	}


	public void setScaledXinMeters(double scaledXinMeters) {
		this.scaledXinMeters = scaledXinMeters;
	}


	public double getScaledYinMeters() {
		return scaledYinMeters;
	}


	public void setScaledYinMeters(double scaledYinMeters) {
		this.scaledYinMeters = scaledYinMeters;
	}

	


	

}
