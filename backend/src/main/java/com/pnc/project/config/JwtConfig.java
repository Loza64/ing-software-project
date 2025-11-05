package com.pnc.project.config;

import com.pnc.project.dto.response.usuario.UsuarioResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtConfig {

    @Value("${security.jwt.expiration-time}")
    private int tokenTime;

    @Value("${security.jwt.secret-key}")
    private String tokenSecret;

    private SecretKey getTokenKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(UsuarioResponse user) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("id", user.getIdUsuario());
        return Jwts.builder()
                .claims(json)
                .signWith(getTokenKey())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenTime))
                .compact();
    }

    public Claims extracClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getTokenKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extracClaims(token);
        return claims == null || claims.getExpiration().before(new Date());
    }
}
