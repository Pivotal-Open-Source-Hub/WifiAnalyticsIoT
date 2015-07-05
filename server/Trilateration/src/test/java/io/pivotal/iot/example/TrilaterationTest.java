package io.pivotal.iot.example;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.junit.Test;

public class TrilaterationTest {
    
	
	@Test public void testTrilateration() {
		
		double[][] positions = new double[][] { { 25.0, 50.0 }, { 0.0, 0.0 }, {50.0, 0.0} };
		double[] distances = new double[] { 35.0, 14.0, 12.0 };

		
		double[] results = Trilateration.calculate(positions, distances);
		double[] expectedPosition = new double[] { 25.0, 10.0 };
		testResultsSimple(expectedPosition, 0.0001, results);		

	}

	private void testResultsSimple(double[] expectedPosition, final double delta, double[] results) {
		StringBuilder output = new StringBuilder("expectedPosition: ");
		for (int i = 0; i < expectedPosition.length; i++) {
			output.append(expectedPosition[i]).append(" ");
		}
		output.append("\n");
		output.append("calculatedPosition: ");
		for (int i = 0; i < results.length; i++) {
			output.append(results[i]).append(" ");
		}
		output.append("\n");
		System.out.println(output.toString());

		
	}
	
	
	private void testResults(double[] expectedPosition, final double delta, Optimum optimum) {

		double[] calculatedPosition = optimum.getPoint().toArray();

		int numberOfIterations = optimum.getIterations();
		int numberOfEvaluations = optimum.getEvaluations();

		StringBuilder output = new StringBuilder("expectedPosition: ");
		for (int i = 0; i < expectedPosition.length; i++) {
			output.append(expectedPosition[i]).append(" ");
		}
		output.append("\n");
		output.append("calculatedPosition: ");
		for (int i = 0; i < calculatedPosition.length; i++) {
			output.append(calculatedPosition[i]).append(" ");
		}
		output.append("\n");

		output.append("numberOfIterations: ").append(numberOfIterations).append("\n");
		output.append("numberOfEvaluations: ").append(numberOfEvaluations).append("\n");
		try {
			RealVector standardDeviation = optimum.getSigma(0);
			output.append("standardDeviation: ").append(standardDeviation).append("\n");
			RealMatrix covarianceMatrix = optimum.getCovariances(0);
			output.append("covarianceMatrix: ").append(covarianceMatrix).append("\n");
		} catch (SingularMatrixException e) {
			System.err.println(e.getMessage());
		}

		System.out.println(output.toString());

		// expected == calculated?
		for (int i = 0; i < calculatedPosition.length; i++) {
			assertEquals(expectedPosition[i], calculatedPosition[i], delta);
		}
		
	}	
	
}
