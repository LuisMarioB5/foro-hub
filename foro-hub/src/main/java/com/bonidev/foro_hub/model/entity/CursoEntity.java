package com.bonidev.foro_hub.model.entity;

import com.bonidev.foro_hub.model.enums.CategoriaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class CursoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nombre;

    @Enumerated(EnumType.STRING)
    CategoriaEnum categoria;

    public CursoEntity(String nombre, CategoriaEnum categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public String mostrar() {
        return String.format("""
                ID: %s
                Nombre: %s
                Categoria: %s
                \n""", this.id, this.nombre, this.categoria.capitalize());
    }
}
