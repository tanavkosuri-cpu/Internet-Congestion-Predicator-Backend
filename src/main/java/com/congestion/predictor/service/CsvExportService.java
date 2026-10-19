
package com.congestion.predictor.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.congestion.predictor.model.HistoricalMeasurement;
import com.congestion.predictor.repository.HistoricalMeasurementRepository;

@Service
public class CsvExportService {

    private final HistoricalMeasurementRepository repository;

    public CsvExportService(
            HistoricalMeasurementRepository repository) {
        this.repository = repository;
    }

    public String exportCsv(int minutes) {

        LocalDateTime startTime =
                LocalDateTime.now().minusMinutes(minutes);

        List<HistoricalMeasurement> records =
                repository
                .findByMeasuredAtGreaterThanEqualOrderByMeasuredAtAsc(
                        startTime);

        StringBuilder csv = new StringBuilder();

        csv.append("id,measured_at,latency,packet_loss,bandwidth,status\n");

        for (HistoricalMeasurement record : records) {

            double latency = record.getLatency();
            double packetLoss = record.getPacketLoss();
            double bandwidth = record.getBandwidth();
            String status = record.getStatus();

            // Export only valid records with known labels.
            if (!Double.isFinite(latency)
                    || !Double.isFinite(packetLoss)
                    || !Double.isFinite(bandwidth)
                    || latency < 0
                    || packetLoss < 0
                    || bandwidth < 0
                    || status == null
                    || !(status.equals("LOW")
                    || status.equals("MEDIUM")
                    || status.equals("HIGH"))) {
                continue;
            }

            csv.append(record.getId()).append(",");
            csv.append(record.getMeasuredAt()).append(",");
            csv.append(String.format(Locale.US, "%.2f", latency))
                    .append(",");
            csv.append(String.format(Locale.US, "%.2f", packetLoss))
                    .append(",");
            csv.append(String.format(Locale.US, "%.2f", bandwidth))
                    .append(",");
            csv.append(status).append("\n");
        }

        return csv.toString();
    }
}