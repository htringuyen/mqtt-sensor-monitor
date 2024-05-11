package org.iotwarehouse.monitorapp.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import org.iotwarehouse.monitorapp.context.Context;
import org.iotwarehouse.monitorapp.core.TimeSeriesController;
import org.iotwarehouse.monitorapp.domain.TimeSeriesReport;
import org.iotwarehouse.monitorapp.domain.ValueRangeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsTimeSeriesMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsTimeSeriesMonitor.class);

    private final Mqtt5Client mqttClient;

    private final String metricsReportTopic;

    private final String metricsControlTopic;

    private final TimeSeriesController controller;

    private final String variableName;

    private final int windowSize;

    private final Double valueRangeUpper;

    private final Double valueRangeLower;

    private final ObjectMapper objectMapper = Context.objectMapper();


    public MetricsTimeSeriesMonitor(Mqtt5Client mqttClient, String metricsReportTopic,
                                    String metricsControlTopic,
                                    TimeSeriesController controller, String variableName,
                                    int windowSize, Double valueRangeUpper, Double valueRangeLower) {
        this.mqttClient = mqttClient;
        this.metricsReportTopic = metricsReportTopic;
        this.metricsControlTopic = metricsControlTopic;
        this.controller = controller;
        this.variableName = variableName;
        this.windowSize = windowSize;
        this.valueRangeUpper = valueRangeUpper;
        this.valueRangeLower = valueRangeLower;

        initController();
    }

    private void initController() {
        controller.setVariableName(variableName);
        controller.setWindowSize(windowSize);
        controller.setValueRangeUpper(valueRangeUpper);
        controller.setValueRangeLower(valueRangeLower);
    }

    private void subscribeToMetricsReportTopic() {
        mqttClient.toAsync().subscribeWith()
                .topicFilter(metricsReportTopic)
                .callback(context -> {
                    var payload = context.getPayloadAsBytes();
                    try {
                        var report = objectMapper.readValue(payload, TimeSeriesReport.class);
                        //LOGGER.debug("Received message of topic {}: {}", metricsReportTopic, report);
                        controller.addDataPoint(report.timestamp(), report.value());
                    } catch (Exception e) {
                        LOGGER.debug("Failed to parse message from topic: {}, message: {}",
                                metricsReportTopic, new String(payload), e);
                    }
                })
                .send();

        LOGGER.info("Subscribed to topic: {}", metricsReportTopic);
    }

    private void registerMetricsSettingHandler() {
        controller.setMetricsSettingHandler((valueRangeUpper, valueRangeLower) -> {
            var command = new ValueRangeCommand("RC", valueRangeLower,
                    valueRangeUpper, "Set upper/lower limit", null);
            try {
                var commandJson = objectMapper.writeValueAsString(command);
                publishControlMessage(commandJson);
            } catch (Exception e) {
                LOGGER.error("Failed to serialize command: {}", command, e);
            }
        });
    }

    public void start() {
        subscribeToMetricsReportTopic();
        registerMetricsSettingHandler();
    }

    public void stop() {
        mqttClient.toAsync().disconnect();
    }

    private void publishControlMessage(String message) {
        mqttClient.toAsync().publishWith()
                .topic(metricsControlTopic)
                .payload(message.getBytes())
                .send()
                .whenComplete((publish, throwable) -> {
                    if (throwable != null) {
                        LOGGER.error("Error occurred while sending control message", throwable);
                    } else {
                        LOGGER.info("Control message sent: {}", message);
                    }
                });
    }

}

























