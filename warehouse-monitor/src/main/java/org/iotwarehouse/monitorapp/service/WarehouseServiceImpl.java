package org.iotwarehouse.monitorapp.service;

import java.util.List;

public class WarehouseServiceImpl implements WarehouseService {

    private final List<String> inlinedWarehouseCodes = List.of("VN-HCM-001", "VN-BD-001", "VN-BD-002");


    WarehouseServiceImpl() {

    }

    @Override
    public List<String> getWarehouseCodes() {
        return inlinedWarehouseCodes;
    }

    @Override
    public boolean isWarehouseCodeValid(String warehouseCode) {
        return inlinedWarehouseCodes.contains(warehouseCode);
    }
}
