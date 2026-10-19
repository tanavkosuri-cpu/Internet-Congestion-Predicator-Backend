
package com.congestion.predictor.model;

public class NetworkMetrics {

    private double latency;
    private double packetLoss;
    private double bandwidth;
    private String status;

    public NetworkMetrics() {
    }

    public NetworkMetrics(double latency, double packetLoss,
                          double bandwidth, String status) {
        this.latency = latency;
        this.packetLoss = packetLoss;
        this.bandwidth = bandwidth;
        this.status = status;
    }

    public double getLatency() {
        return latency;
    }

    public void setLatency(double latency) {
        this.latency = latency;
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}