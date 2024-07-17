package com.bonidev.foro_hub.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private final String JWT_SECRET = System.getenv("JWT_SECRET");
    private final Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);

    public String generarToken(UsuarioEntity usuario) {
        try {
            return JWT.create()
                    .withIssuer("Foro Hub")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(5).toInstant(ZoneOffset.of("-04:00"));
    }

    public String getSubject(String token) {
        if (token == null) {
            throw new RuntimeException("El token no puede ser nulo.");
        }

        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(algorithm)
                    .withIssuer("Foro Hub")
                    .build()
                    .verify(token);

        } catch (JWTVerificationException exception) {
            throw new RuntimeException(exception.getMessage());
        }

        assert decodedJWT != null;
        return decodedJWT.getSubject();
    }
}
