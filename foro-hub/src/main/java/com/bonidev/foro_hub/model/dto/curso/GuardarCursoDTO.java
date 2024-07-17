package com.bonidev.foro_hub.model.dto.curso;

import com.bonidev.foro_hub.model.enums.CategoriaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuardarCursoDTO(
        @NotBlank
        String nombre,

        @NotNull
        CategoriaEnum categoria
) {
}
