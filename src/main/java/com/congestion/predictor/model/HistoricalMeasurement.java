
package com.congestion.predictor.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "historical_measurements")
public class HistoricalMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latency;
    private double packetLoss;
    private double bandwidth;
    private String status;
    private LocalDateTime measuredAt;

    public HistoricalMeasurement() {
    }

    public HistoricalMeasurement(
            double latency,
            double packetLoss,
            double bandwidth,
            String status,
            LocalDateTime measuredAt) {

        this.latency = latency;
        this.packetLoss = packetLoss;
        this.bandwidth = bandwidth;
        this.status = status;
        this.measuredAt = measuredAt;
    }

    public Long getId() {
        return id;
    }

    public double getLatency() {
        return latency;
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }
}