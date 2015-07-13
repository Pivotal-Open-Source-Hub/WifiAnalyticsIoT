package io.pivotal.demo.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
@EnableAutoConfiguration

public class WebConsoleApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebConsoleApp.class, args);
    }
}
