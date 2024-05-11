package org.iotwarehouse.monitorapp.service;

import java.util.List;

public interface WarehouseService {

    List<String> getWarehouseCodes();

    boolean isWarehouseCodeValid(String warehouseCode);
}
