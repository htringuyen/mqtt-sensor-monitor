package org.iotwarehouse.monitorapp;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.iotwarehouse.monitorapp.controller.WarehouseSystemController;
import org.iotwarehouse.monitorapp.core.Controller;
import org.iotwarehouse.monitorapp.core.View;
import org.iotwarehouse.monitorapp.monitor.MetricsTimeSeriesMonitor;
import org.iotwarehouse.monitorapp.service.ApplicationServices;
import org.iotwarehouse.monitorapp.service.WarehouseService;
import org.iotwarehouse.monitorapp.simulation.TimeSeriesSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WarehouseMonitorApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseMonitorApp.class);

    private static final String MQTT_HOST = "::";

    private static final int MQTT_PORT = 1883;

    private Stage primaryStage;

    private MetricsTimeSeriesMonitor metricsTimeSeriesMonitor;

    private final List<TimeSeriesSensor> timeSeriesSensors = new ArrayList<>();

    private final ExecutorService es = Executors.newCachedThreadPool();

    private final WarehouseService warehouseService = ApplicationServices.warehouseService();

    private Controller mainController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Warehouse Monitor");
        initMain();
    }


    private void initMain() {

        var view = View.of(WarehouseSystemController.class, VBox.class);

        this.mainController = view.controller();

        primaryStage.setScene(new Scene(view.component()));

        runMetricsSensors();

        this.primaryStage.show();
    }

    @Override
    public void stop() throws Exception {

        mainController.destroy();

        for (var sensor : timeSeriesSensors) {
            sensor.stop();
        }

        es.shutdown();
        if (es.awaitTermination(5, TimeUnit.SECONDS)) {
            LOGGER.info("Executor service gracefully terminated");
        } else {
            throw new TimeoutException("Executor service shutdown timeout");
        }
    }

    private void runMetricsSensors() {
        for (var warehouseCode : warehouseService.getWarehouseCodes()) {

            // create temperature sensor
            var temperatureSensor = new TimeSeriesSensor(warehouseCode, "Temperature", "C", "temperature_system",
                    1000, 25, 36, createTimeSeriesSensorClient());

            // create humidity sensor
            var humiditySensor = new TimeSeriesSensor(warehouseCode, "Humidity", "%", "humidity_system",
                    1000, 40, 60, createTimeSeriesSensorClient());

            es.submit(temperatureSensor);
            es.submit(humiditySensor);
            timeSeriesSensors.add(temperatureSensor);
            timeSeriesSensors.add(humiditySensor);
        }
    }


    public Mqtt5Client createTimeSeriesSensorClient() {
        var client =  Mqtt5Client.builder()
                .serverHost(MQTT_HOST)
                .serverPort(MQTT_PORT)
                .build();
        client.toBlocking().connect();
        return client;
    }
}























