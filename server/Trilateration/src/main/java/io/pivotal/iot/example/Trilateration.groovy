package io.pivotal.iot.example

	
	import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.css.CalculatedValue;
	
	
	
	
	public class Trilateration {
	
		
		/**
		 * Determining distance from decibel level
		 * There’s a useful concept in physics that lets us mathematically relate the signal level in dB to a real-world distance.  Free-space path loss (FSPL) characterizes how the wireless signal degrades over distance (following an inverse square law):
		 * FSPL(Db) = 20*log10(d)+ 20*log10(f) + 27.55
		 */
		 public static double calculateDistanceInMeters(double levelInDb, double freqInMHz)    {
			double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
			return Math.pow(10.0, exp);
		 }
		 
		
		
		
						
			 static double calcDistance(double rssi) {
					  double base = 10;
					  double exponent = -(rssi + 51.504)/16.532;
					  //double distance = Math.pow(base, exponent);
					  //104.09004338 + 13.26842562x + 0.57250833x^2 + 0.00986120x^3 + 0.00006099x^4
					  
					  // SI NORTH THIRD FLOOR (room 3250)
	//                double distance = 104.09004338 + 13.26842562 * rssi + 0.57250833* Math.pow(rssi,2)
	//                      + 0.00986120*Math.pow(rssi, 3) + 0.00006099 * Math.pow(rssi,4);
					  
					  // SI NORTH FIRST FLOOR
					  // 0 degree
					  //double distance = 3324.4981666 + 234.0366524 * rssi + 6.0593624* Math.pow(rssi,2)
				  //  + 0.0683264*Math.pow(rssi, 3) + 0.0002843 * Math.pow(rssi,4);
					  
					  double distance = 730.24198315 + 52.33325511*rssi + 1.35152407*Math.pow(rssi, 2)
							  + 0.01481265*Math.pow(rssi, 3) + 0.00005900*Math.pow(rssi, 4)+0.00541703*180;
					  
					  
					  //return (distance>0)?distance:rssi;
					  return distance;
			 }
			 
			 // Convert Feet into Meter
			 static double calFeetToMeter(double rssi) {
					 return rssi*0.3048;
			 }
			 
			 // Description of calDistToDeg function
			 //
			 // To get the myLocation, rssi distance should be converted into
			 // latitude and longitude unit.
			 // This function convert rssi distance into lat long decimal unit.
			 //
			 static double calDistToDeg(double dist) {
					 double result;
					 double DistToDeg;
	
					 final int lat = 42;
					 final double EarthRadius = 6367449;
					 final double a = 6378137;
					 final double b = 6356752.3;
					 final double ang = lat*(Math.PI/180);
					 
					 // This function will calculate the longitude distance based on the latitude
					 // More information is
					 // http://en.wikipedia.org/wiki/Geographic_coordinate_system#Expressing_latitude_and_longitude_as_linear_units
					 
	//               result = Math.cos(ang)*Math.sqrt((Math.pow(a,4)*(Math.pow(Math.cos(ang),2))
	//                               + (Math.pow(b,4)*(Math.pow(Math.sin(ang),2))))
	//                               / (Math.pow((a*Math.cos(ang)),2)+Math.pow((b*Math.sin(ang)),2)))
	//                               * Math.PI/180;
					 
					 DistToDeg = 82602.89223259855;  // unit (meter), based on 42degree.
					 result = dist/DistToDeg;                // convert distance to lat,long degree.
					 return result;
					 
			 }
			 
			 static double getLongitude(double Lat1, double Long1, double rssi1,
																					double Lat2, double Long2, double rssi2,
																					double Lat3, double Long3, double rssi3) {
					 double dist1, dist2, dist3;
					 double MyLat, MyLong;
					 
					 dist1 = calDistToDeg(10);      //calDistToDeg(calcDistance(rssi1));
					 dist2 = calDistToDeg(12);      //calDistToDeg(calcDistance(rssi2));
					 dist3 = calDistToDeg(8);       //calDistToDeg(calcDistance(rssi3));
	
					 MyLong = (2*(Lat3-Lat1)*(Math.pow(dist2,2)-Math.pow(dist1,2))
											-2*(Lat2-Lat1)*(Math.pow(dist3,2)-Math.pow(dist1,2)))
											/ (4*(Lat2-Lat1)*(Long3-Long1)-4*(Lat3-Lat1)*(Long2-Long1));
					 
					 return MyLong;
			 }
			 
			 static double getLatitude(double Lat1, double Long1, double rssi1,
									double Lat2, double Long2, double rssi2,
									double Lat3, double Long3, double rssi3) {
			
					 double magnitude = 100000000;
					 double dist1, dist2, dist3;
					 double MyLat, MyLong;
					 
					 dist1 = calDistToDeg(calcDistance(rssi1));
					 dist2 = calDistToDeg(calcDistance(rssi2));
					 dist3 = calDistToDeg(calcDistance(rssi3));
					 
					 MyLat = (2*(Long2-Long1)*(Math.pow(dist3,2)-Math.pow(dist1,2))
											- 2*(Long3-Long1)*(Math.pow(dist2,2)-Math.pow(dist1,2)))
											/ (4*((Lat2-Lat1)*(Long3-Long1)-(Lat3-Lat1)*(Long2-Long1)));
					
					 return MyLat;
			}
			 
	
			 static double[] myRotation(double x, double y, double dist, double deg) {
					 
					 double tmpX, tmpY;
					 //ArrayList<Double> myLocation = null;
					 double[]  myLocation = new double[3];
					 
					 tmpX = x*Math.cos((Math.PI/180)*deg)-y*Math.sin((Math.PI/180)*deg);
					 tmpY = x*Math.sin((Math.PI/180)*deg)+y*Math.cos((Math.PI/180)*deg);
					 
	//               myLocation.add(tmpX);
	//               myLocation.add(tmpY);
					 myLocation[0] = tmpX;
					 myLocation[1] = tmpY;
					 myLocation[2] = dist;
					 
					 return myLocation;
			 }
			 
			static double[] MyTrilateration(double Lat1, double Long1, double rssi1,
									double Lat2, double Long2, double rssi2,
									double Lat3, double Long3, double rssi3) {
					 
					 //ArrayList<Double> tmpWAP1, tmpWAP2, tmpWAP3;
					 double[] tmpWAP1 = new double[3];
					 double[] tmpWAP2 = new double[3];
					 double[] tmpWAP3 = new double[3];
					 
					 double dist1, dist2, dist3;
					 double tmpLat2, tmpLong2, tmpLat3, tmpLong3;
					 double tmpSlide, deg;
					 double MyLat, MyLong;
					 
					 double[] MyLocation = new double[2];
					 
	//               dist1 = calDistToDeg(5);       //calDistToDeg(calcDistance(rssi1));
	//               dist2 = calDistToDeg(6);       //calDistToDeg(calcDistance(rssi2));
	//               dist3 = calDistToDeg(7);       //calDistToDeg(calcDistance(rssi3));
					 
					 dist1 = calDistToDeg(calFeetToMeter(calcDistance(rssi1)));
					 dist2 = calDistToDeg(calFeetToMeter(calcDistance(rssi2)));
					 dist3 = calDistToDeg(calFeetToMeter(calcDistance(rssi3)));
					 
					 //test
	//               dist1 = calDistToDeg(calFeetToMeter(53));
	//               dist2 = calDistToDeg(calFeetToMeter(24));
	//               dist3 = calDistToDeg(calFeetToMeter(51));
									 
					 tmpLat2        = Lat2-Lat1;
					 tmpLong2       = Long2 - Long1;
					 tmpLat3        = Lat3-Lat1;
					 tmpLong3       = Long3 - Long1;
					 
					 tmpSlide = Math.sqrt(Math.pow(tmpLat2,2)+Math.pow(tmpLong2,2));
					 
					 //deg = (180/Math.PI)*Math.acos( ((Math.pow(tmpLat2,2) + Math.pow(tmpSlide,2) - Math.pow(tmpLong2, 2)) / (2*tmpLat2*tmpSlide)) );
					 deg = (180/Math.PI)*Math.acos( Math.abs(tmpLat2)/Math.abs(tmpSlide));
					 
					 // 1 quadrant
					 if( (tmpLat2>0 && tmpLong2>0) ) {
							 deg = 360 - deg;
					 }
					 else if( (tmpLat2<0 && tmpLong2>0) ) {
							 deg = 180 + deg;
					 }
					 // 3 quadrant
					 else if( (tmpLat2<0 && tmpLong2<0)){
							 deg = 180 - deg;
					 }
					 // 4 quadrant
					 else if( (tmpLat2>0 && tmpLong2<0)) {
							 deg = deg;
					 }
					 
					 tmpWAP1[0] = 0.0;
					 tmpWAP1[1] = 0.0;
					 tmpWAP1[2] = dist1;
					 tmpWAP2 = myRotation(tmpLat2, tmpLong2, dist2, deg);
					 tmpWAP3 = myRotation(tmpLat3, tmpLong3, dist3, deg);
					 
	
					 MyLat = (Math.pow(tmpWAP1[2],2)-Math.pow(tmpWAP2[2],2)+Math.pow(tmpWAP2[0],2))/(2*tmpWAP2[0]);
					 
					 MyLong = (Math.pow(tmpWAP1[2],2)-Math.pow(tmpWAP3[2],2)-Math.pow(MyLat,2)
									 +Math.pow(MyLat-tmpWAP3[0],2)+Math.pow(tmpWAP3[1], 2))/(2*tmpWAP3[1]);
					 
					 MyLocation = myRotation(MyLat, MyLong, 0, -deg);
					 
					 MyLocation[0] = MyLocation[0] + Lat1;
					 MyLocation[1] = MyLocation[1] + Long1;
					 
					 return MyLocation;
			 }
									
									
			public static void main(String[] args){
				
				double distance1 = calculateDistanceInMeters(-82, 5180);
				double distance2 = calculateDistanceInMeters(-54, 5180);
				double distance3 = calculateDistanceInMeters(-32, 5180);
				
				
				System.out.println("Distance 1: "+distance1);
				System.out.println("Distance 2: "+distance2);
				System.out.println("Distance 3: "+distance3);
				
				

				
				
			}
									
																					
	}
	
	

