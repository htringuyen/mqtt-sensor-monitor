USE warehousesys;
GO

INSERT INTO SensorSystemInfo(warehouseId, temperatureSystemSensorTotal,
                                       humiditySystemSensorTotal, sideTemperatureSensorTotal,
                                       sideHumiditySensorTotal, containerStatusSensorEach)
SELECT wh.id, 1, 1, 4, 4, 1
FROM Warehouse as wh