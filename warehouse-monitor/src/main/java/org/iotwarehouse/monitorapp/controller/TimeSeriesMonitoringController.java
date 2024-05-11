package org.iotwarehouse.monitorapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import org.iotwarehouse.monitorapp.core.MetricsSettingHandler;
import org.iotwarehouse.monitorapp.core.TimeSeriesController;
import org.iotwarehouse.monitorapp.util.chart.TimestampStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.ResourceBundle;

public class TimeSeriesMonitoringController implements TimeSeriesController, Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSeriesMonitoringController.class);

    private static final String FXML_NAME = "TimeSeriesMonitoringController";

    private static final long DEFAULT_X_AXIS_RANGE = 60_000L;

    private static final long DEFAULT_X_TICK_UNIT = 2_000L;

    private static final String RANGE_UPPER = "Range upper";

    private static final String RANGE_LOWER = "Range lower";

    private static final double Y_SCALE_RANGE_UPPER = 1.4;

    private static final double Y_SCALE_RANGE_LOWER = 1.4;

    @FXML
    private ToggleButton autoButton;

    @FXML
    private TextField minValueTextField;

    @FXML
    private TextField maxValueTextField;

    @FXML
    private Button onOffButton;

    @FXML
    private Button metricsSettingButton;

    @FXML
    private Text settingInfoLabel;

    @FXML
    private LineChart<Number, Number> timeseriesLineChart;

    private String variableName;

    private int windowSize = 10;

    private Double valueRangeUpper;

    private Double valueRangeLower;

    private ObservableList<XYChart.Data<Number, Number>> dataPoints;

    private ObservableList<XYChart.Data<Number, Number>> rangeUpperPoints;

    private ObservableList<XYChart.Data<Number, Number>> rangeLowerPoints;

    private MetricsSettingHandler metricsSettingHandler;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.timeseriesLineChart.setTitle("Temperature monitor");
        this.timeseriesLineChart.setAnimated(false);

        // setup x-axis
        var xAxis = getXAxis();
        xAxis.forceZeroInRangeProperty().set(false);
        xAxis.setAutoRanging(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setAnimated(false);
        xAxis.setLabel("Time");
        xAxis.setTickLabelFormatter(new TimestampStringConverter());

        // setup y-axis
        var yAxis = getYAxis();
        yAxis.setAnimated(false);
        yAxis.setForceZeroInRange(false);
        yAxis.setLabel("Value");

        // setup data point list
        this.dataPoints = FXCollections.observableArrayList();
        var dataSeries = new XYChart.Series<>(getVariableName(), this.dataPoints);
        this.timeseriesLineChart.getData().add(dataSeries);

        // setup range upper point list
        this.rangeUpperPoints = FXCollections.observableArrayList();
        var rangeUpperSeries = new XYChart.Series<>(RANGE_UPPER, this.rangeUpperPoints);
        this.timeseriesLineChart.getData().add(rangeUpperSeries);

        // setup range lower point list
        this.rangeLowerPoints = FXCollections.observableArrayList();
        var rangeLowerSeries = new XYChart.Series<>(RANGE_LOWER, this.rangeLowerPoints);
        this.timeseriesLineChart.getData().add(rangeLowerSeries);

        // ranging xAxis
        rangingXAxis();
    }

    private NumberAxis getXAxis() {
        return (NumberAxis) timeseriesLineChart.getXAxis();
    }

    private NumberAxis getYAxis() {
        return (NumberAxis) timeseriesLineChart.getYAxis();
    }

    @Override
    public void setVariableName(String name) {
        this.variableName = name;
    }

    private String getVariableName() {
        return variableName == null ? "TimeSeries variable" : variableName;
    }

    @Override
    public void setValueRangeUpper(Double value) {
        this.valueRangeUpper = value;
        var yAxis = getYAxis();
        yAxis.setUpperBound(value * Y_SCALE_RANGE_UPPER);
        updateValueRangeUpper();
    }

    private Double getValueRangeUpper() {
        return this.valueRangeUpper;
    }

    @Override
    public void setValueRangeLower(Double value) {
        this.valueRangeLower = value;
        var yAxis = getYAxis();
        yAxis.setLowerBound(value * Y_SCALE_RANGE_LOWER);
        updateValueRangeLower();
    }

    public Double getValueRangeLower() {
        return this.valueRangeLower;
    }

    @Override
    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    private int getWindowSize() {
        return windowSize;
    }

    @Override
    public void setMetricsSettingHandler(MetricsSettingHandler handler) {
        this.metricsSettingHandler = handler;
    }

    @Override
    public void addDataPoint(Long timestamp, Double value) {
        registerFXTask(() -> {
            var point = newXYPoint(timestamp, value);
            if (dataPoints.size() >= getWindowSize()) {
                dataPoints.removeFirst();
            }
            dataPoints.add(point);
            rangingXAxis();
            updateValueRangeLower();
            updateValueRangeUpper();
        });
    }

    private void updateValueRangeUpper() {
        // check value range upper change
        var upperValue = getValueRangeUpper();
        if (upperValue == null) {
            return;
        }

        /*rangeUpperPoints.clear();
        rangeUpperPoints.add(newXYPoint(getXAxisLowerBound(), upperValue));
        rangeUpperPoints.add(newXYPoint(getXAxisUpperBound(), upperValue));*/
        addNewRangePoint(rangeUpperPoints, upperValue);
    }

    private void updateValueRangeLower() {
        var lowerValue = getValueRangeLower();
        if (lowerValue == null) {
            return;
        }

        /*rangeLowerPoints.clear();
        rangeLowerPoints.add(newXYPoint(getXAxisLowerBound(), lowerValue));
        rangeLowerPoints.add(newXYPoint(getXAxisUpperBound(), lowerValue));*/
        addNewRangePoint(rangeLowerPoints, lowerValue);
    }

    private void addNewRangePoint(List<XYChart.Data<Number, Number>> points, Double value) {
        if (points.size() >= 2) {
            // check if two last point have the same value
            var lastPoint = points.get(points.size() - 1);
            var secondLastPoint = points.get(points.size() - 2);

            if (lastPoint.getYValue().doubleValue()
                    == value && secondLastPoint.getYValue().doubleValue()
                    == value) {
                points.removeLast();
                points.add(newXYPoint(getXAxisUpperBound(), value));
            } else {
                points.add(newXYPoint(getXAxisUpperBound(), value));
            }

            if (points.size() >= windowSize) {
                points.removeFirst();
            }

        } else {
            points.add(newXYPoint(getXAxisLowerBound(), value));
            points.add(newXYPoint(getXAxisUpperBound(), value));
        }
    }

    private void rangingXAxis() {
        var xAxis = getXAxis();

        xAxis.setLowerBound(getXAxisLowerBound());
        xAxis.setUpperBound(getXAxisUpperBound());
        xAxis.setTickUnit(DEFAULT_X_TICK_UNIT);
    }

    private long getXAxisUpperBound() {
        if (dataPoints.isEmpty()) {
            return Instant.now().toEpochMilli();
        } else {
            return dataPoints.getLast().getXValue().longValue();
        }
    }

    private long getXAxisLowerBound() {
        if (dataPoints.size() <= 1) {
            return getXAxisUpperBound() - DEFAULT_X_AXIS_RANGE;
        } else {
            return dataPoints.getFirst().getXValue().longValue();
        }
    }

    private XYChart.Data<Number, Number> newXYPoint(Long timestamp, Double value) {
        return new XYChart.Data<>(timestamp, value);
    }

    // set handler for metrics setting button
    @FXML
    void onMetricsSettingButtonPressed(ActionEvent event) {
        var minValue = getDoubleValue(minValueTextField);
        if (minValue == null) {
            settingInfoLabel.setText("Invalid min value");
            return;
        }

        var maxValue = getDoubleValue(maxValueTextField);
        if (maxValue == null) {
            settingInfoLabel.setText("Invalid max value");
            return;
        }

        if (metricsSettingHandler != null) {
            metricsSettingHandler.onMetricsSetting(minValue, maxValue);
        }

        settingInfoLabel.setText("Metrics setting applied");

        setValueRangeLower(minValue);
        setValueRangeUpper(maxValue);
    }

    private Double getDoubleValue(TextField textField) {
        try {
            return Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
            return null;
        }
    }



}





































