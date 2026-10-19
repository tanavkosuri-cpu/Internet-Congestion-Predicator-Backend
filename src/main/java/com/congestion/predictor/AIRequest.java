
package com.congestion.predictor;

public class AIRequest {

    private double latency;
    private double packet_loss;
    private double bandwidth;

    public AIRequest(double latency, double packetLoss, double bandwidth) {
        this.latency = latency;
        this.packet_loss = packetLoss;
        this.bandwidth = bandwidth;
    }

    public double getLatency() {
        return latency;
    }

    public double getPacket_loss() {
        return packet_loss;
    }

    public double getBandwidth() {
        return bandwidth;
    }
}