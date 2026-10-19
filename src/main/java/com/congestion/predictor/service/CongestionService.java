
package com.congestion.predictor.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.congestion.predictor.model.HistoricalMeasurement;
import com.congestion.predictor.model.NetworkMetrics;
import com.congestion.predictor.repository.HistoricalMeasurementRepository;

@Service
public class CongestionService {

    private final NetworkMonitoringService networkMonitoringService;
    private final HistoricalMeasurementRepository repository;

    private volatile NetworkMetrics latestMetrics =
            new NetworkMetrics(-1, -1, -1, "UNKNOWN");

    private volatile LocalDateTime lastAttemptAt;
    private volatile LocalDateTime lastSuccessfulAt;
    private volatile int consecutiveFailures = 0;
    private volatile String lastError = "Monitoring has not started";

    public CongestionService(
            NetworkMonitoringService networkMonitoringService,
            HistoricalMeasurementRepository repository) {
        this.networkMonitoringService = networkMonitoringService;
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 300000, initialDelay = 0)
    public void collectMetrics() {

        lastAttemptAt = LocalDateTime.now();

        try {
            NetworkMetrics metrics =
                    networkMonitoringService.measureNetwork();

            if (metrics == null) {
                throw new IllegalStateException(
                        "Network monitoring returned no measurements");
            }

            double latency = metrics.getLatency();
            double packetLoss = metrics.getPacketLoss();
            double bandwidth = metrics.getBandwidth();

            // Reject incomplete measurements for AI training.
            if (latency < 0 || packetLoss < 0 || bandwidth < 0) {
                latestMetrics = new NetworkMetrics(
                        latency, packetLoss, bandwidth, "UNKNOWN");

                consecutiveFailures++;
                lastError = "Incomplete network measurement";
                System.out.println("Measurement skipped: " + lastError);
                return;
            }

            String status;

            if (latency > 150 || packetLoss > 5) {
                status = "HIGH";
            } else if (latency > 80 || packetLoss > 2) {
                status = "MEDIUM";
            } else {
                status = "LOW";
            }

            metrics.setStatus(status);
            latestMetrics = metrics;

            HistoricalMeasurement record =
                    new HistoricalMeasurement(
                            latency,
                            packetLoss,
                            bandwidth,
                            status,
                            LocalDateTime.now());

            repository.save(record);

            lastSuccessfulAt = LocalDateTime.now();
            consecutiveFailures = 0;
            lastError = "";

            System.out.println(
                    "Saved measurement: " + status
                    + ", latency: " + latency + " ms");

        } catch (Exception e) {
            consecutiveFailures++;
            lastError = e.getClass().getSimpleName()
                    + ": " + String.valueOf(e.getMessage());

            System.err.println(
                    "Network monitoring failed: " + lastError);
        }
    }

    public NetworkMetrics getMetrics() {
        return latestMetrics;
    }

    public List<HistoricalMeasurement> getHistory(
            LocalDateTime startTime) {

        return repository
                .findByMeasuredAtGreaterThanEqualOrderByMeasuredAtAsc(
                        startTime);
    }

    public Map<String, Object> getMonitoringStatus() {

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("lastAttemptAt", lastAttemptAt);
        result.put("lastSuccessfulAt", lastSuccessfulAt);
        result.put("consecutiveFailures", consecutiveFailures);
        result.put("lastError", lastError);

        String status;

        if (lastSuccessfulAt == null) {
            status = "STARTING_OR_FAILED";
        } else if (consecutiveFailures > 0) {
            status = "DEGRADED";
        } else {
            status = "OK";
        }

        result.put("monitoringStatus", status);

        return result;
    }
}