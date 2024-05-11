package org.iotwarehouse.monitorapp.domain;

import lombok.NonNull;

import java.time.Instant;

public record ValueRangeCommand(String code, Double lowerValue, Double upperValue, String description, Long timestamp) {

    public ValueRangeCommand(@NonNull String code, @NonNull Double lowerValue,
                             @NonNull Double upperValue, String description, Long timestamp) {

        if (!code.equals("RC")) {
            throw new IllegalArgumentException("Invalid code: " + code);
        }

        this.code = code;
        this.lowerValue = lowerValue;
        this.upperValue = upperValue;
        this.description = description == null ? "" : description;
        this.timestamp = timestamp == null ? Instant.now().toEpochMilli() : timestamp;
    }
}
