package org.iotwarehouse.monitorapp.domain;

import lombok.NonNull;

import java.time.Instant;

public record TimeSeriesReport(String code, Double value, String unit, Long timestamp, String description) {

    private static final String TIME_SERIES_CODE = "TS";

    public TimeSeriesReport(@NonNull String code, @NonNull Double value, String unit, Long timestamp, String description) {

        if (!code.equals(TIME_SERIES_CODE)) {
            throw new IllegalArgumentException("Invalid code: " + code);
        }

        this.code = code;
        this.value = value;
        this.unit = unit == null ? "Celsius" : unit;
        this.timestamp = timestamp == null ? Instant.now().toEpochMilli() : timestamp;
        this.description = description == null ? "Time series report" : description;
    }
}
