package com.bonidev.foro_hub.model.dto.topico;

import com.bonidev.foro_hub.model.entity.CursoEntity;
import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuardarTopicoDTO(
        @NotBlank
        String titulo,

        @NotBlank
        String mensaje,

        @NotNull
        UsuarioEntity autor,

        @NotNull
        CursoEntity curso) {}
