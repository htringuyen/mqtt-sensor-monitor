package org.iotwarehouse.monitorapp.controller;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.iotwarehouse.monitorapp.core.Controller;
import javafx.scene.control.Label;
import org.iotwarehouse.monitorapp.core.View;
import org.iotwarehouse.monitorapp.monitor.MetricsTimeSeriesMonitor;

import java.net.URL;
import java.util.ResourceBundle;

public class WarehouseMonitorController implements Controller, Initializable {

    private static final int CHART_WINDOW_SIZE = 25;

    private static final double DEFAULT_TEMPERATURE_LOWER = 25.0;

    private static final double DEFAULT_TEMPERATURE_UPPER = 36.0;

    private static final double DEFAULT_HUMIDITY_LOWER = 40.0;

    private static final double DEFAULT_HUMIDITY_UPPER = 60.0;

    private static final String HUMIDITY_VAR = "Humidity";

    private static final String HUMIDITY_UNIT = "%";

    private static final String TEMPERATURE_VAR = "Temperature";

    private static final String TEMPERATURE_UNIT = "°C";

    private static final String TEMPERATURE_SYSTEM_TOPIC_GROUP = "temperature_system";

    private static final String HUMIDITY_SYSTEM_TOPIC_GROUP = "humidity_system";

    private static final String REPORT_TOPIC_TYPE = "report";

    private static final String CONTROL_TOPIC_TYPE = "control";


    @FXML
    private GridPane monitorGridPane;

    @FXML
    private Label warehouseInfoLabel;

    @Setter
    private String warehouseCode;

    @Setter
    private Mqtt5Client mqtt5Client;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monitorGridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    @Override
    public void postInitialize() {
        this.warehouseInfoLabel.setText("Warehouse: " + this.warehouseCode);
        setupChartMonitors();
    }


    private String getReportTopic(String topicGroup) {
        return warehouseCode + "/" + topicGroup + "/" + REPORT_TOPIC_TYPE;
    }

    private String getControlTopic(String topicGroup) {
        return warehouseCode + "/" + topicGroup + "/" + CONTROL_TOPIC_TYPE;
    }

    private void setupChartMonitors() {
        // two monitor in the same row of the grid
        monitorGridPane.add(createMetricsChart(TEMPERATURE_VAR, TEMPERATURE_UNIT, TEMPERATURE_SYSTEM_TOPIC_GROUP,
                DEFAULT_TEMPERATURE_LOWER, DEFAULT_TEMPERATURE_UPPER), 0, 0);

        monitorGridPane.add(createMetricsChart(HUMIDITY_VAR, HUMIDITY_UNIT, HUMIDITY_SYSTEM_TOPIC_GROUP, DEFAULT_HUMIDITY_LOWER,
                DEFAULT_HUMIDITY_UPPER), 1, 0);

        monitorGridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private Node createMetricsChart(String variableName, String variableUnit, String topicGroup, double lower, double upper) {

        var view = View.of(TimeSeriesMonitoringController.class, VBox.class);

        view.component().setMaxSize(2000, 2000);

        view.component().prefHeightProperty().set(2000);

        var monitor = new MetricsTimeSeriesMonitor(
                mqtt5Client,
                getReportTopic(topicGroup),
                getControlTopic(topicGroup),
                view.controller(),
                variableName, variableUnit,
                CHART_WINDOW_SIZE,
                upper, lower);

        monitor.start();

        return view.component();
    }
}





























