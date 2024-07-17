package com.bonidev.foro_hub.security.dto;

import jakarta.validation.constraints.NotBlank;

public record AutenticacionDTO(
        @NotBlank
        String email,

        @NotBlank
        String password) {}
