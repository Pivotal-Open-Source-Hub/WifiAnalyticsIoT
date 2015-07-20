package io.pivotal.demo.ui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

public class AppInitializer extends SpringBootServletInitializer {

    
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebConsoleApp.class);
    }
}