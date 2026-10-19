
package com.congestion.predictor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CongestionPredictorApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                CongestionPredictorApplication.class, args);
    }
}