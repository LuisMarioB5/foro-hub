package com.bonidev.foro_hub.model.entity;

import com.bonidev.foro_hub.model.dto.topico.GuardarTopicoDTO;
import com.bonidev.foro_hub.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "topicos", uniqueConstraints = @UniqueConstraint(columnNames = {"titulo", "mensaje"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TopicoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String titulo;

    @Column(nullable = false)
    String mensaje;

    LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    StatusEnum status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    UsuarioEntity autor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id")
    CursoEntity curso;

    public TopicoEntity(String titulo, String mensaje, LocalDateTime fechaCreacion, StatusEnum status, UsuarioEntity autor, CursoEntity curso) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaCreacion = fechaCreacion;
        this.status = status;
        this.autor = autor;
        this.curso = curso;
    }

    public String mostrar() {
        return String.format("""
                Titulo: %s
                Mesaje: %s
                Nombre del Autor: %s
                Nombre del Curso: %s
                """, this.titulo, this.mensaje, this.autor.getNombre(), this.curso.getNombre());
    }
}
