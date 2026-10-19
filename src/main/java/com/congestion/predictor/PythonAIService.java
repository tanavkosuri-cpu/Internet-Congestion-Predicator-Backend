
package com.congestion.predictor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PythonAIService {

    private final RestClient restClient;

    public PythonAIService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://127.0.0.1:8000")
                .build();
    }

    public AIResponse predict(AIRequest request) {
        return restClient.post()
                .uri("/predict")
                .body(request)
                .retrieve()
                .body(AIResponse.class);
    }
}