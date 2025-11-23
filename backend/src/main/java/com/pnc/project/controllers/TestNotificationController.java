package com.pnc.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class TestNotificationController {

    @GetMapping("/success")
    public ResponseEntity<?> testSuccess() {
        return ResponseEntity.ok("Notificación exitosa desde el backend 🚀");
    }

    @GetMapping("/error")
    public ResponseEntity<?> testError() {
        return ResponseEntity.status(500).body("Error simulado desde el backend ❌");
    }
}
