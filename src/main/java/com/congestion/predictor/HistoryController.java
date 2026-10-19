
package com.congestion.predictor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.congestion.predictor.model.HistoricalMeasurement;
import com.congestion.predictor.repository.HistoricalMeasurementRepository;

@RestController
@RequestMapping("/api/history")

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
    "http://localhost:5184"

})
public class HistoryController {

    private final HistoricalMeasurementRepository repository;

    public HistoryController(
            HistoricalMeasurementRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<HistoricalMeasurement> getHistory(
            @RequestParam(defaultValue = "60") int minutes) {

        int safeMinutes = Math.max(1, Math.min(minutes, 10080));

        LocalDateTime startTime =
                LocalDateTime.now().minusMinutes(safeMinutes);

        return repository
                .findByMeasuredAtGreaterThanEqualOrderByMeasuredAtAsc(
                        startTime);
    }
}