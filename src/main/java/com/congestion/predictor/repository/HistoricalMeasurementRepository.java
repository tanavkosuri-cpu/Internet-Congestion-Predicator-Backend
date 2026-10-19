
package com.congestion.predictor.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.congestion.predictor.model.HistoricalMeasurement;

public interface HistoricalMeasurementRepository
        extends JpaRepository<HistoricalMeasurement, Long> {

    List<HistoricalMeasurement> findByMeasuredAtGreaterThanEqualOrderByMeasuredAtAsc(
            LocalDateTime startTime);

    Optional<HistoricalMeasurement> findTopByOrderByMeasuredAtDesc();
}

