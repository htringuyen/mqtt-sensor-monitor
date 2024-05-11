package org.iotwarehouse.monitorapp.simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import org.iotwarehouse.monitorapp.context.Context;
import org.iotwarehouse.monitorapp.domain.TimeSeriesReport;
import org.iotwarehouse.monitorapp.domain.ValueRangeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class TimeSeriesSensor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSeriesSensor.class);

    private static final double ERROR_EPSILON = 0.1;

    private static final String REPORT_CODE = "TS";

    private final Random random = new Random();

    private final int reportInterval;

    private final Mqtt5Client mqttClient;

    private final String warehouseCode;
    
    private final String variableName;

    private final String variableUnit;

    private final String sensorType;

    private final ObjectMapper objectMapper = Context.objectMapper();

    private final AtomicBoolean shouldRun = new AtomicBoolean(true);

    private final AtomicLong minValueBits = new AtomicLong();

    private final AtomicLong maxValueBits = new AtomicLong();


    public TimeSeriesSensor(String warehouseCode, String variableName, String variableUnit, String sensorType,
                            int reportInterval, double minTemperature, double maxTemperature, Mqtt5Client mqttClient) {
        this.warehouseCode = warehouseCode;
        this.variableName = variableName;
        this.variableUnit = variableUnit;
        this.sensorType = sensorType;
        this.reportInterval = reportInterval;
        setMinValue(minTemperature);
        setMaxValue(maxTemperature);
        this.mqttClient = mqttClient;

        subscribeControlTopic();
    }

    private String getReportTopic() {
        return warehouseCode + "/" + sensorType + "/report";
    }

    private String getControlTopic() {
        return warehouseCode + "/" + sensorType + "/control";
    }

    private void setMinValue(double value) {
        this.minValueBits.set(Double.doubleToLongBits(value));
    }

    private void setMaxValue(double value) {
        this.maxValueBits.set(Double.doubleToLongBits(value));
    }

    private double getMinValue() {
        return Double.longBitsToDouble(minValueBits.get());
    }

    private double getMaxValue() {
        return Double.longBitsToDouble(maxValueBits.get());
    }

    private String getUnit() {
        return variableUnit;
    }

    private String getReportDescription() {
        return variableName + " report";
    }

    private void subscribeControlTopic() {
        mqttClient.toAsync().subscribeWith()
                .topicFilter(getControlTopic())
                .callback(publish -> {
                    try {
                        var command = objectMapper.readValue(publish.getPayloadAsBytes(), ValueRangeCommand.class);
                        setMinValue(command.lowerValue());
                        setMaxValue(command.upperValue());
                    } catch (Exception e) {
                        LOGGER.error("Error occurred while processing control message", e);
                    }
                })
                .send()
                .whenComplete((subAck, throwable) -> {
                    if (throwable != null) {
                        LOGGER.error("Error occurred while subscribing to control topic", throwable);
                    } else {
                        LOGGER.info("Subscribed to control topic: {}", getControlTopic());
                    }
                });
    }

    @Override
    public void run() {
        while (shouldRun.get()) {
            try {
                double value = generateArtificialValue();

                var report = new TimeSeriesReport(REPORT_CODE, value, getUnit(), null, getReportDescription());
                var reportJson = objectMapper.writeValueAsString(report);

                mqttClient.toAsync().publishWith()
                        .topic(getReportTopic())
                        .payload(reportJson.getBytes())
                        .send()
                        .whenComplete((publish, throwable) -> {
                            if (throwable != null) {
                                LOGGER.error("Error occurred while sending {}", getReportDescription(), throwable);
                            } else {
                                LOGGER.debug("Sent to topic {}: {}", getReportTopic(), reportJson);
                            }
                        });

                Thread.sleep(reportInterval);
            } catch (Exception e) {
                LOGGER.error("Error occurred while sending {}", getReportDescription(), e);
            }
        }
    }

    public void stop() {
        shouldRun.set(false);
        mqttClient.toAsync().disconnect();
        Thread.currentThread().interrupt();
    }

    private double generateArtificialValue() {
        var error = ERROR_EPSILON * (getMaxValue() - getMinValue());
        error = random.nextBoolean() ? - error : error;
        return getMinValue() + Math.random() * (getMaxValue() - getMinValue()) + error;
    }
}


















