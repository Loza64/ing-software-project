package com.pnc.project.service.impl;

import com.pnc.project.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Value("${app.backend.url:http://localhost:4000}")
    private String backendUrl;

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            log.info("Preparando envío de email de recuperación de contraseña a: {}", to);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recuperación de Contraseña");

            // Codificar el token para la URL (los tokens JWT pueden contener caracteres especiales)
            String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
            
            // Construir la URL de reset (usar frontend si está configurado)
            String resetUrl = frontendUrl + "/reset-password?token=" + encodedToken;
            
            String emailBody = buildPasswordResetEmailBody(token, resetUrl);
            helper.setText(emailBody, true); // true indica que es HTML

            log.info("Enviando email de recuperación de contraseña a: {}", to);
            mailSender.send(message);
            log.info("Email de recuperación de contraseña enviado exitosamente a: {}", to);
            
        } catch (MessagingException e) {
            log.error("Error de mensajería al enviar email de recuperación de contraseña a: {}", to, e);
            throw new RuntimeException("Error al enviar el email de recuperación de contraseña: " + e.getMessage(), e);
        } catch (MailException e) {
            log.error("Error de Spring Mail al enviar email de recuperación de contraseña a: {}", to, e);
            throw new RuntimeException("Error al enviar el email de recuperación de contraseña: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error inesperado al enviar email de recuperación de contraseña a: {}", to, e);
            throw new RuntimeException("Error inesperado al enviar el email de recuperación de contraseña: " + e.getMessage(), e);
        }
    }

    private String buildPasswordResetEmailBody(String token, String resetUrl) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 20px; background-color: #f9f9f9; }" +
                ".button { display: inline-block; padding: 12px 24px; background-color: #4CAF50; " +
                "color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                ".token { background-color: #e8e8e8; padding: 10px; border-radius: 5px; " +
                "font-family: monospace; word-break: break-all; margin: 10px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Recuperación de Contraseña</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Hemos recibido una solicitud para restablecer tu contraseña.</p>" +
                "<p><strong>Para restablecer tu contraseña:</strong></p>" +
                "<ol style='padding-left: 20px;'>" +
                "<li>Copia el siguiente token de recuperación</li>" +
                "<li>Pégalo en la ventana de recuperación de contraseña de la aplicación</li>" +
                "<li>Ingresa tu nueva contraseña</li>" +
                "</ol>" +
                "<p style='margin-top: 20px;'><strong>Tu token de recuperación es:</strong></p>" +
                "<div class='token'>" + token + "</div>" +
                "<p style='margin-top: 20px;'>O puedes hacer clic en el siguiente enlace:</p>" +
                "<p style='text-align: center;'>" +
                "<a href='" + resetUrl + "' class='button'>Ir a Restablecer Contraseña</a>" +
                "</p>" +
                "<p><strong>⚠️ Importante: Este token expirará en 15 minutos.</strong></p>" +
                "<p style='color: #666; font-size: 14px;'>Si no solicitaste este cambio, puedes ignorar este email de forma segura.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>Este es un email automático, por favor no respondas.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

