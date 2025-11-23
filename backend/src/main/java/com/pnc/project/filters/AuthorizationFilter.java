package com.pnc.project.filters;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.project.dto.response.ExceptionResponse;
import com.pnc.project.entities.Usuario;
import com.pnc.project.service.impl.PermissionService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final PermissionService permissionService;

    public AuthorizationFilter(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated()) {

                String method = request.getMethod();
                String requestPath = request.getRequestURI();

                Usuario user = (Usuario) auth.getPrincipal();
                Long roleId = user.getRol().getIdRol();

                boolean pathAllowedByDB = permissionService.hasPermission(roleId, requestPath, method);

                if (!pathAllowedByDB) {
                    deny(response, "Ruta no permitida para tu rol en BD: " + requestPath);
                    return;
                }

                boolean userHasAuthority = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(method + ":" + requestPath)
                                || a.getAuthority().equals("ROLE_ADMIN"));

                if (!userHasAuthority) {
                    deny(response, "No tienes el permiso requerido para esta ruta: " + requestPath);
                    return;
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            deny(response, ex.getMessage());
        }
    }

    private void deny(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ExceptionResponse error = new ExceptionResponse(
                HttpServletResponse.SC_FORBIDDEN,
                message);

        String json = new ObjectMapper().writeValueAsString(error);
        response.getWriter().write(json);
    }
}
