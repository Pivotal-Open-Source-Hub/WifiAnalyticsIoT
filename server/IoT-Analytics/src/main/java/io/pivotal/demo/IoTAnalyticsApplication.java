package io.pivotal.demo;


//@SpringBootApplication
public class IoTAnalyticsApplication {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(IoTAnalyticsApplication.class, args);
    	ProbeCaptureRunner runner = new ProbeCaptureRunner();
    	runner.run(args);
    }
}
