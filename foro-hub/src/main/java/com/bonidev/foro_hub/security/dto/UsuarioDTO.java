package com.bonidev.foro_hub.security.dto;

import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
        @NotBlank
        String email,

        @NotBlank
        String password) {
    public UsuarioDTO(UsuarioEntity user) {
        this(user.getEmail(), user.getPassword());
    }
}
