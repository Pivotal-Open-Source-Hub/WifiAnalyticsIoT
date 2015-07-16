package io.pivotal.demo.trilateration;


import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
	
	
	
	
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
		 		 
		 /*
		  * Calculate the TrilaterationG using NonLinearLeastSquareSolver
		  */
		 public static double[] calculate(double[][] positions, double[] distances){
			 try{
				 TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positions, distances);
				 NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());
		 
				 Optimum optimum = solver.solve();
				 return optimum.getPoint().toArray();
			 }
			 catch(Exception e){
				 System.out.println("Could not update position: "+e.getMessage());
				 return new double[]{0.0,0.0};
			 }
		 }
					
		/* 
		 public static void main (String[] args){
			 
			 System.out.println("Expected distance for 21 dbm:" + calculateDistanceInMeters(21, 2412));
			 System.out.println("Expected distance for 23 dbm:" + calculateDistanceInMeters(23, 2412));
			 System.out.println("Expected distance for 31 dbm:" + calculateDistanceInMeters(31, 2412));
			 System.out.println("Expected distance for 35 dbm:" + calculateDistanceInMeters(35, 2412));
			 System.out.println("Expected distance for 37 dbm:" + calculateDistanceInMeters(37, 2412));
			 System.out.println("Expected distance for 41 dbm:" + calculateDistanceInMeters(41, 2412));
			 System.out.println("Expected distance for 43 dbm:" + calculateDistanceInMeters(43, 2412));
			 System.out.println("Expected distance for 51 dbm:" + calculateDistanceInMeters(51, 2412));
			 System.out.println("Expected distance for 61 dbm:" + calculateDistanceInMeters(61, 2412));
		 }
			*/																		
	}
	
	

