package org.iotwarehouse.monitorapp.context;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Context {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }
}
