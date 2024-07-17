package com.bonidev.foro_hub.model.dto.topico;

import com.bonidev.foro_hub.model.entity.CursoEntity;
import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import com.bonidev.foro_hub.model.enums.StatusEnum;

import java.time.LocalDateTime;

public record MostrarTopicoDTO(
        String titulo,

        String mensaje,

        LocalDateTime fechaCreacion,

        String estado,

        UsuarioEntity autor,

        CursoEntity curso) {}
