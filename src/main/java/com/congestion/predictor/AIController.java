package com.congestion.predictor;

import com.congestion.predictor.model.HistoricalMeasurement;
import com.congestion.predictor.repository.HistoricalMeasurementRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {
	    "http://localhost:5173",
	    "http://localhost:5174",
	    "http://localhost:5175",
	    "http://localhost:5176",
	    "http://localhost:5177",
	    "http://localhost:5178",
	    "http://localhost:5179",
	    "http://localhost:5180",
	    "http://localhost:5181",
	    "http://localhost:5182",
	    "http://localhost:5183",
	    "http://localhost:5184",
	    "http://localhost:5185",
	    "http://localhost:5186",
	    "http://localhost:5187",
	    "http://localhost:5188",
	    "http://localhost:5189",
	    "http://localhost:5190"







	})
@RestController
public class AIController {

    private final PythonAIService pythonAIService;
    private final HistoricalMeasurementRepository repository;

    public AIController(
            PythonAIService pythonAIService,
            HistoricalMeasurementRepository repository) {

        this.pythonAIService = pythonAIService;
        this.repository = repository;
    }

    // Predict using manually entered values
    @GetMapping("/api/ai/predict")
    public AIResponse predict(
            @RequestParam double latency,
            @RequestParam double packetLoss,
            @RequestParam double bandwidth) {

        AIRequest request =
                new AIRequest(latency, packetLoss, bandwidth);

        return pythonAIService.predict(request);
    }

    // Predict using the latest saved network measurement
    @GetMapping("/api/ai/predict/latest")
    public AIResponse predictLatest() {

        HistoricalMeasurement measurement =
                repository.findTopByOrderByMeasuredAtDesc()
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "No network measurements found"));

        AIRequest request = new AIRequest(
                measurement.getLatency(),
                measurement.getPacketLoss(),
                measurement.getBandwidth()
        );

        return pythonAIService.predict(request);
    }
}