package com.pnc.project.controllers;

import com.pnc.project.dto.Message;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.SerializationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;


@ControllerAdvice
public class AdviceController {

    // 400 - Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Message> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Message(false, "Validation error: " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<Message> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Message(false, "Validation error: " + ex.getConstraintViolations().iterator().next().getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<Message> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Message(false, "Missing parameter: " + ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<Message> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Message(false, "Type mismatch for parameter '" + ex.getName() + "': " + ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseBody
    public ResponseEntity<Message> handleMissingServletRequestPart(MissingServletRequestPartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Message(false, "Missing request part: " + ex.getRequestPartName()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Message> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Message(false, ex.getMessage()));
    }

    // 401 - Unauthorized
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<Message> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new Message(false, "Authentication failed: " + ex.getMessage()));
    }

    // 403 - Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<Message> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new Message(false, "Access denied: " + ex.getMessage()));
    }

    // 404 - Not Found
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Message> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Message(false, ex.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResponseEntity<Message> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Message(false, "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL()));
    }

    // 409 - Conflict
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<Message> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new Message(false, "Data integrity violation: " + ex.getRootCause().getMessage()));
    }

    // 500 - Internal Server Error
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public ResponseEntity<Message> handleSqlException(SQLException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Message(false, "Database error: " + ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public ResponseEntity<Message> handleDataAccessException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Message(false, "Data access error: " + ex.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Message> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Message(false, "JWT processing error: " + ex.getMessage()));
    }

    @ExceptionHandler(SerializationException.class)
    public ResponseEntity<Message> handleException(SerializationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Message(false, "Serialization error: " + ex.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResponseEntity<Message> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Message(false, "Null reference error: " + ex.getMessage()));
    }
}