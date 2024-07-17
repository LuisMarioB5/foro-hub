package com.bonidev.foro_hub.security.controller;

import com.bonidev.foro_hub.security.dto.AutenticacionDTO;
import com.bonidev.foro_hub.security.dto.JWTTokenDTO;
import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import com.bonidev.foro_hub.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AutenticacionController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<JWTTokenDTO> autenticarUsuario(@RequestBody @Valid AutenticacionDTO userAuth) {
        try {
            System.out.println("Inicio del proceso de autenticación");
            Authentication token = new UsernamePasswordAuthenticationToken(userAuth.email(), userAuth.password());
            Authentication usuarioAuth = authenticationManager.authenticate(token);
            System.out.println("Autenticación exitosa para usuario: " + usuarioAuth.getName());
            var JWTToken = tokenService.generarToken((UsuarioEntity) usuarioAuth.getPrincipal());
            System.out.println("Token JWT generado correctamente");
            return ResponseEntity.ok(new JWTTokenDTO(JWTToken));
        } catch (AuthenticationException e) {
            System.out.println("Error durante la autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
