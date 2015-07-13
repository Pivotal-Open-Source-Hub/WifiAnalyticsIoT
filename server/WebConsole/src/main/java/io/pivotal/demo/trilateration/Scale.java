package io.pivotal.demo.trilateration;

import io.pivotal.demo.ui.DeviceLocation;
import io.pivotal.demo.ui.GeodeClient;

import java.util.ArrayList;
import java.util.List;

public class Scale {

	private static double scaleX=1;
	private static double scaleY=1;
	
	
	
	public static void updateScale(){
		
		List<DeviceLocation> pis = new ArrayList(GeodeClient.getInstance().getPIsLocation());
		
		double sX=0, sY=0;
		for (int i=0; i<pis.size()-1; i++){
			
			for (int j=i+1; j<pis.size(); j++){
				double distanceX = Math.abs(pis.get(i).getX() - pis.get(j).getX());
				double distanceY = Math.abs(pis.get(i).getY() - pis.get(j).getY());
				
				if(distanceX > sX) sX = distanceX / 100 ;
				if(distanceY > sY) sY = distanceY / 100 ;
				
			}
		}
		scaleX = sX;
		scaleY = sY;
		/*
		System.out.println("ScaleX = "+scaleX);
		System.out.println("ScaleY = "+scaleY);
		System.out.println("Scale = "+ ((Scale.getX() + Scale.getY())/2) );
		*/
		 
						
	}
	
	public static double getX(){
		return scaleX;
	}
	
	public static double getY(){
		return scaleY;
	}

}
