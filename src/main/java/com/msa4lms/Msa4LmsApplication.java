package com.msa4lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Msa4LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Msa4LmsApplication.class, args);
    }

}
