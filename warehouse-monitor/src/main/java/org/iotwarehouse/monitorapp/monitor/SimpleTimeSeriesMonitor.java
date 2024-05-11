package org.iotwarehouse.monitorapp.monitor;

import org.iotwarehouse.monitorapp.core.TimeSeriesController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleTimeSeriesMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTimeSeriesMonitor.class);

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Random random = new Random();

    private final TimeSeriesController controller;

    public SimpleTimeSeriesMonitor(TimeSeriesController controller) {
        this.controller = controller;
        controller.setWindowSize(20);
        controller.setVariableName("Temperature");
        controller.setValueRangeLower(26.0);
        controller.setValueRangeUpper(31.0);
    }

    public void startMonitoringDaemon() {
        scheduler.scheduleWithFixedDelay(this::updateNewData, 1, 2, TimeUnit.SECONDS);
    }

    public void stopMonitoringDaemon() {
        scheduler.shutdown();
        try {
            if (scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                LOGGER.debug("Monitoring daemon stopped gracefully.");
            } else {
                throw new TimeoutException("Monitoring daemon did not stop after timeout");
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException("Failed to stop monitoring daemon");
        }
    }

    private void updateNewData() {
        var epochMilli = Instant.now().toEpochMilli();
        var value = randomInRange(27, 32);
        controller.addDataPoint(epochMilli, value);
        LOGGER.debug("Data point added: ({}, {})", epochMilli, value);
    }

    private Double randomInRange(double from, double to) {
        return from + random.nextDouble() * (to - from);
    }
}
























