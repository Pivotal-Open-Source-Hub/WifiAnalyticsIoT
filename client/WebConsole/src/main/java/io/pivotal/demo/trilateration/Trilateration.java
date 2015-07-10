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
					
																					
	}
	
	

