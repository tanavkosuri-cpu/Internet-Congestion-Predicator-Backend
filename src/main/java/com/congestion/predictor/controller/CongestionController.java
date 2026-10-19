
package com.congestion.predictor.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.congestion.predictor.model.HistoricalMeasurement;
import com.congestion.predictor.model.NetworkMetrics;
import com.congestion.predictor.service.CongestionService;
import com.congestion.predictor.service.CsvExportService;

@RestController
public class CongestionController {

    private final CongestionService congestionService;
    private final CsvExportService csvExportService;

    public CongestionController(
            CongestionService congestionService,
            CsvExportService csvExportService) {
        this.congestionService = congestionService;
        this.csvExportService = csvExportService;
    }

    @GetMapping("/api/congestion")
    public NetworkMetrics getCongestion() {
        return congestionService.getMetrics();
    }

    @GetMapping("/api/congestion/history")
    public List<HistoricalMeasurement> getHistory(
            @RequestParam(defaultValue = "60") int minutes) {

        int safeMinutes = Math.max(1, Math.min(minutes, 10080));

        LocalDateTime startTime =
                LocalDateTime.now().minusMinutes(safeMinutes);

        return congestionService.getHistory(startTime);
    }

    @GetMapping("/api/congestion/monitoring")
    public Map<String, Object> getMonitoringStatus() {
        return congestionService.getMonitoringStatus();
    }

    @GetMapping(value = "/api/congestion/export", produces = "text/csv")
    public ResponseEntity<String> exportCsv(
            @RequestParam(defaultValue = "1440") int minutes) {

        int safeMinutes = Math.max(1, Math.min(minutes, 10080));

        String csv = csvExportService.exportCsv(safeMinutes);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=congestion_data.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}