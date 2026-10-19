
package com.congestion.predictor;

public class AIResponse {

    private String congestionStatus;
    private boolean anomalyDetected;
    private double anomalyScore;
    private double latency;
    private double packetLoss;
    private double bandwidth;
    private String message;

    public String getCongestionStatus() {
        return congestionStatus;
    }

    public void setCongestionStatus(String congestionStatus) {
        this.congestionStatus = congestionStatus;
    }

    public boolean isAnomalyDetected() {
        return anomalyDetected;
    }

    public void setAnomalyDetected(boolean anomalyDetected) {
        this.anomalyDetected = anomalyDetected;
    }

    public double getAnomalyScore() {
        return anomalyScore;
    }

    public void setAnomalyScore(double anomalyScore) {
        this.anomalyScore = anomalyScore;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}