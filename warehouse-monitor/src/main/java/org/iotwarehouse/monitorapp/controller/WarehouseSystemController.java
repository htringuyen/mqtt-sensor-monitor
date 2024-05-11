package org.iotwarehouse.monitorapp.controller;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.iotwarehouse.monitorapp.core.Controller;
import org.iotwarehouse.monitorapp.core.View;
import org.iotwarehouse.monitorapp.domain.ObserverAuth;
import org.iotwarehouse.monitorapp.service.ApplicationServices;
import org.iotwarehouse.monitorapp.service.WarehouseService;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class WarehouseSystemController implements Controller, Initializable {

    private static final String MQTT_HOST = "::";

    private static final int MQTT_PORT = 1883;

    @FXML
    private Button openTabButton;

    @FXML
    private TextField warehouseCodeTextField;

    @FXML
    private TabPane warehouseMonitorTabPane;

    private final WarehouseService warehouseService = ApplicationServices.warehouseService();

    private ObserverAuth observerAuth;

    private final Mqtt5Client mqttClient;

    private final Map<String, Tab> warehouseMonitorTabs = new HashMap<>();


    public WarehouseSystemController() {
        this.mqttClient = createMqttClient();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        warehouseMonitorTabPane.getTabs().clear();
    }


    @FXML
    void onOpenTabButtonPressed(ActionEvent event) {
        var warehouseCode = getWarehouseCode();

        if (warehouseService.isWarehouseCodeValid(warehouseCode)
                && !warehouseMonitorTabs.containsKey(warehouseCode)) {
            var tab = createWarehouseMonitorTab(warehouseCode);
            warehouseMonitorTabPane.getTabs().add(tab);
            warehouseMonitorTabs.put(warehouseCode, tab);
        }
    }


    private String getWarehouseCode() {
        return warehouseCodeTextField.getText();
    }

    private Tab createWarehouseMonitorTab(String warehouseCode) {

        var view = View.of(WarehouseMonitorController.class, VBox.class);

        var controller = view.controller();
        controller.setWarehouseCode(warehouseCode);
        controller.setMqtt5Client(mqttClient);
        controller.postInitialize();

        var tab = new Tab(warehouseCode, view.component());
        tab.setClosable(true);
        tab.setText(warehouseCode);
        return tab;
    }

    private Mqtt5Client createMqttClient() {
        var client =  Mqtt5Client.builder()
                .serverHost(MQTT_HOST)
                .serverPort(MQTT_PORT)
                .build();

        client.toBlocking().connect();;
        return client;
    }

    @Override
    public void destroy() {
        mqttClient.toBlocking().disconnect();
    }
}




















