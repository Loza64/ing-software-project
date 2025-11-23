package com.pnc.project.filters;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.project.dto.response.ExceptionResponse;
import com.pnc.project.entities.Usuario;
import com.pnc.project.service.UsuarioService;
import com.pnc.project.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtUtil;
    private final UsuarioService userService;

    public AuthenticationFilter(JwtConfig jwtUtil, UsuarioService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.equals("/api/auth/login")
                || path.equals("/api/forgot-password")
                || path.equals("/api/validate-reset-token")
                || path.equals("/api/reset-password")
                || path.equals("/api/save")
                || path.equals("/api/registros/test/horas")
                || path.startsWith("/notifications");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Bearer token requerido");
            return;
        }

        final String token = authHeader.substring(7);

        try {
            if (jwtUtil.isTokenExpired(token)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
                return;
            }

            Claims claims = jwtUtil.extracClaims(token);
            if (claims == null) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o sin claims");
                return;
            }

            Integer userId = jwtUtil.extractIdUser(token);
            if (userId == null) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Token inválido: ID de usuario ausente");
                return;
            }

            Usuario user = userService.infoAuthById(userId);
            if (user == null) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Tu cuenta ha sido eliminada");
                return;
            }

            Set<GrantedAuthority> authorities = user.getRol().getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getMethod() + ":" + permission.getPath()))
                    .collect(Collectors.toSet());

            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRol().getNombre()));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
        } catch (JwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        ExceptionResponse error = new ExceptionResponse(status, message);
        String json = new ObjectMapper().writeValueAsString(error);
        response.getWriter().write(json);
    }
}
