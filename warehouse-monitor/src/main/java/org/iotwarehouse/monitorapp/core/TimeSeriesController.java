package org.iotwarehouse.monitorapp.core;

public interface TimeSeriesController extends Controller {

    void setVariableName(String variableName);

    void addDataPoint(Long timestamp, Double value);

    void setValueRangeUpper(Double value);

    void setValueRangeLower(Double value);

    void setWindowSize(int windowSize);

    void setMetricsSettingHandler(MetricsSettingHandler handler);
}
