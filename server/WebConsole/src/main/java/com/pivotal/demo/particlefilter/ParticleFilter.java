package com.pivotal.demo.particlefilter;

import io.pivotal.demo.ui.DeviceDistance;
import io.pivotal.demo.ui.DeviceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ParticleFilter {

	private static int numberOfParticles = 2000;
	private static int movementStdDev = 15;
	private static double signalRelativeStdDev = 0.15;
	
	private static final int maxX = 800;
	private static final int maxY = 800;
	
	private Particle[] particles = new Particle[numberOfParticles];
	
	private String deviceId;
	
	Map<DeviceDistance,DeviceLocation> deviceDistancePerPI;

	
	static class Particle implements Comparable<Particle>{
		
		int x, y;
		double p;
		
		Particle(){
		}
		
		Particle(int x, int y, double p){
			this.x = x;
			this.y = y;
			this.p = p;
		}
		
		@Override
		public int compareTo(Particle o) {
			return Double.compare(this.p, o.p);
		}
		
		
	}
	
	public ParticleFilter(String deviceId, Map<DeviceDistance,DeviceLocation> deviceDistancePerPI){
		
		this.deviceId = deviceId;
		this.deviceDistancePerPI = deviceDistancePerPI;
		
	    for (int i = 0; i < numberOfParticles; i++) {
	    	
	    	particles[i] = new Particle();
	    	particles[i].x = (int) Math.round(Math.random() * maxX);
	    	particles[i].y = (int) Math.round(Math.random() * maxY);
	    	particles[i].p = 1;
	    	
	    }
	    
		
		
	}
	
	public DeviceLocation calculate(){
		
		predictParticles();
		update();
		resample();
		return guess();
				
	}
	


	/*
	 * Predict next position of each the particle
	 * as we don't know direction or the speed of moving target we assume that it can move in every direction
	 *  according to some gaussian distribution, in future we could try to detect user movement based on other
	 *   sensors e.g. accelerometer
	 */
	private void predictParticles() {
		
	    for (int i = 0; i < numberOfParticles; i++) {
	    	double[] movedParticle = randomPairFromGaussian(particles[i], movementStdDev);
            particles[i].x = (int) Math.round(movedParticle[0]);
            particles[i].y = (int) Math.round(movedParticle[1]);
	    }		
        
	}
	
	/*
	 *  Calculate probabilities for every particle
	 */
	private void update() {
		
		//for each raspberry
		Iterator<DeviceDistance> distances = deviceDistancePerPI.keySet().iterator();
		while (distances.hasNext()){
			
			 // let's assume that while we account for bigger error when the raspberry is further from the particle
			 DeviceDistance distance = distances.next(); 
			 DeviceLocation pi = deviceDistancePerPI.get(distance);
			 double stdDev = distance.getDistance() * signalRelativeStdDev;
			 double mean = 0;
			 int i=0;				
			 //for each particle, measure the cumulative density
			 for (int j=0; j<particles.length; j++){
				 double particleDistance = distance(pi, particles[j]);
				 particles[i].p *= Gaussian.Phi(-1 * Math.abs(particleDistance - distance.getDistance()), mean, stdDev);
				 i++;
			 }
			 			
			 
		}
		
        // normalize sum of all probabilities to 1
        double pSum = 0;
        
		for (int j=0; j<particles.length; j++){
			pSum += particles[j].p;
		}
		for (int j=0; j<particles.length; j++){
            particles[j].p = particles[j].p / pSum;		
		}
		
				
	}
	
	//resample particles based on probabilties
	private void resample() {
		// see. http://robotics.stackexchange.com/questions/479/particle-filters-how-to-do-resampling
        
		
		// order by probability
		List<Particle> orderedParticles = new ArrayList<Particle>();
		for (int i = 0; i < particles.length; i++) {
			orderedParticles.add(particles[i]);
		}
		Collections.sort(orderedParticles);
		Collections.reverse(orderedParticles);
		
		particles = orderedParticles.toArray(new Particle[0]);
		
		// calculate sums
		double[] cumulativeSums = new double[particles.length];
        for (int i = 0; i < particles.length; i++) {
            double p = particles[i].p;
            if (i == 0) {
                cumulativeSums[i]=p;
            } else {
                cumulativeSums[i] = cumulativeSums[i - 1] + p;
            }
        }
		
        // rasample
        List<Particle> oldParticles = Arrays.asList(particles);        
        List<Particle> newParticlesList = new ArrayList<Particle>();
        
        Iterator<Particle> particlesIt = oldParticles.iterator();
        while(particlesIt.hasNext()){
        	Particle particle = particlesIt.next();
            double random = Math.random();
            double previousBin = 0;
            
            // TODO: binary search here
            int index=0;
            for (int i = 0; i < cumulativeSums.length; i++) {
                if (random > previousBin && random < cumulativeSums[i]) {
                    break;
                }
                previousBin = cumulativeSums[i];
                index=i;
            }
            newParticlesList.add(new Particle(oldParticles.get(index).x, oldParticles.get(index).y, oldParticles.get(index).p));            
        	
        }
        
        particles = newParticlesList.toArray(new Particle[newParticlesList.size()]);
	}
	
	// average over all points to get our estimated position
	private DeviceLocation guess() {
		DeviceLocation estimatedLocation = new DeviceLocation();

		int estimatedX = 0;
		int estimatedY = 0;
        for (int i=0; i<particles.length; i++){
        	estimatedX += particles[i].x;
        	estimatedY += particles[i].y;
        }
		
        estimatedLocation.setX(estimatedX / particles.length);
        estimatedLocation.setY(estimatedY / particles.length);
        estimatedLocation.setDeviceId(deviceId);
        
        return estimatedLocation;
	}
		
	
		
	private double distance(DeviceLocation a, Particle b) {
	    return Math.sqrt(distanceSquared(a, b));
	}

	private double distanceSquared(DeviceLocation a, Particle b) {
	    return Math.pow(a.getX() - b.x, 2) + Math.pow(a.getY() - b.y, 2);
	}	
	
	
	private double[] randomPairFromGaussian(Particle p, double stdev) {
	    double u = Math.random();
	    double v = Math.random();
	    return new double[]{
	        p.x + Math.sqrt(-2 * Math.log(u)) * Math.sin(2 * Math.PI * v) * stdev,
	        p.y + Math.sqrt(-2 * Math.log(u)) * Math.cos(2 * Math.PI * v) * stdev
	    };
	}	
}
