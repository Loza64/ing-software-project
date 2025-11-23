package com.pnc.project.controllers;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {

            String controller = handlerMethod.getBeanType().getSimpleName();
            String method = handlerMethod.getMethod().getName();

            System.out.println("Controller usado: " + controller);
            System.out.println("Método ejecutado: " + method);
        }

        return true;
    }
}
