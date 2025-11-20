package com.pnc.project.service;

public interface EmailService {
    /**
     * Envía un email con el token de recuperación de contraseña
     * @param to Email del destinatario
     * @param token Token JWT para recuperación de contraseña
     */
    void sendPasswordResetEmail(String to, String token);
}

