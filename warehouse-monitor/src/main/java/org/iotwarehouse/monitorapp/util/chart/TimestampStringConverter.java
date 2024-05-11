package org.iotwarehouse.monitorapp.util.chart;

import javafx.util.StringConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimestampStringConverter extends StringConverter<Number> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final ZoneId zoneId = ZoneId.systemDefault();

    @Override
    public String toString(Number number) {
        var instant = Instant.ofEpochMilli(number.longValue());
        var dateTime = LocalDateTime.ofInstant(instant, zoneId);
        return dateTime.format(formatter);
    }

    @Override
    public Number fromString(String s) {
        throw new IllegalStateException("Not implemented yet.");
    }
}
