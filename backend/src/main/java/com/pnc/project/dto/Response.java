package com.pnc.project.dto;

import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public  class Response {
    public static ResponseEntity<Object> build(int status, String message, Object data) {
        boolean state = status >= 200 && status <= 299;
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("state", state);
        response.put("message", message);
        if (data != null) {
            response.put("result", data);
        }
        return ResponseEntity.status(status).body(response);
    }
}
