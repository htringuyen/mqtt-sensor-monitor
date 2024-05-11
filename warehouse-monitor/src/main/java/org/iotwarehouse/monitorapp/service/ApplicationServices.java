package org.iotwarehouse.monitorapp.service;

public class ApplicationServices {

    private static final AuthenticationService authenticationService = new AuthenticationServiceImpl();

    private static final WarehouseService warehouseService = new WarehouseServiceImpl();

    public static AuthenticationService authenticationService() {
        return authenticationService;
    }

    public static WarehouseService warehouseService() {
        return warehouseService;
    }
}
