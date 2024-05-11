package org.iotwarehouse.monitorapp.core;

@FunctionalInterface
public interface MetricsSettingHandler {

    void onMetricsSetting(Double valueRangeUpper, Double valueRangeLower);
}
