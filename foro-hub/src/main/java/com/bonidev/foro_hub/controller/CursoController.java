package com.bonidev.foro_hub.controller;

import com.bonidev.foro_hub.model.dto.curso.GuardarCursoDTO;
import com.bonidev.foro_hub.model.entity.CursoEntity;
import com.bonidev.foro_hub.repository.CursoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private final CursoRepository repository;

    @Autowired
    public CursoController(CursoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> guardarCurso(@RequestBody @Valid GuardarCursoDTO cursoDTO) {

        if (cursoDTO.toString().isEmpty()) {
            return ResponseEntity.badRequest().body("El Curso debe contener datos.");
        }
        System.out.println(cursoDTO.nombre() + '\n' + cursoDTO.categoria());
        CursoEntity curso = new CursoEntity(cursoDTO.nombre(), cursoDTO.categoria());
        repository.save(curso);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(curso.getId())
                .toUri();

        return ResponseEntity.created(location).body("El curso fue almacenado satisfactoriamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> mostrarCurso(@PathVariable Long id) {
        CursoEntity curso = repository.getReferenceById(id);

        return ResponseEntity.ok(curso.mostrar());
    }
}

