
package com.congestion.predictor.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.congestion.predictor.model.NetworkMetrics;

@Service
public class NetworkMonitoringService {

    private static final int PACKETS_TO_SEND = 4;

    public NetworkMetrics measureNetwork() {

        int receivedPackets = 0;
        double totalLatency = 0;
        boolean pingCompleted = false;

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "ping", "-n", String.valueOf(PACKETS_TO_SEND),
                    "-w", "1000", "1.1.1.1");

            builder.redirectErrorStream(true);

            Process process = builder.start();

            Pattern timePattern = Pattern.compile(
                    "time[=<]\\s*(\\d+(?:\\.\\d+)?)\\s*ms",
                    Pattern.CASE_INSENSITIVE);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream(),
                            StandardCharsets.UTF_8))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    if (line.contains("TTL=")
                            || line.contains("ttl=")) {

                        Matcher matcher = timePattern.matcher(line);

                        if (matcher.find()) {
                            receivedPackets++;
                            totalLatency += Double.parseDouble(
                                    matcher.group(1));
                        } else if (line.matches("(?i).*time<1ms.*")) {
                            receivedPackets++;
                            totalLatency += 0.5;
                        }
                    }
                }
            }

            pingCompleted = process.waitFor() == 0;

        } catch (Exception e) {
            System.err.println(
                    "Ping measurement failed: " + e.getMessage());
        }

        double latency = -1;
        double packetLoss = -1;

        if (pingCompleted) {
            latency = receivedPackets > 0
                    ? totalLatency / receivedPackets
                    : -1;

            packetLoss =
                    ((PACKETS_TO_SEND - receivedPackets)
                    * 100.0) / PACKETS_TO_SEND;
        }

        double bandwidth = measureDownloadSpeed();

        return new NetworkMetrics(
                latency,
                packetLoss,
                bandwidth,
                "UNKNOWN");
    }

    private double measureDownloadSpeed() {

        HttpURLConnection connection = null;

        try {
            URL url = new URL(
                    "https://speed.cloudflare.com/__down?bytes=1000000");

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("GET");

            long startTime = System.nanoTime();
            long bytesRead = 0;

            try (var input = connection.getInputStream()) {
                byte[] buffer = new byte[8192];
                int count;

                while ((count = input.read(buffer)) != -1) {
                    bytesRead += count;
                }
            }

            double seconds =
                    (System.nanoTime() - startTime)
                    / 1_000_000_000.0;

            if (seconds <= 0 || bytesRead == 0) {
                return -1;
            }

            return (bytesRead * 8.0)
                    / (seconds * 1_000_000.0);

        } catch (Exception e) {
            System.err.println(
                    "Download speed measurement failed: "
                    + e.getMessage());

            return -1;

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}