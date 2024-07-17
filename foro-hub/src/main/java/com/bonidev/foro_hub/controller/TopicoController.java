package com.bonidev.foro_hub.controller;

import com.bonidev.foro_hub.errors.ResourceNotFoundException;
import com.bonidev.foro_hub.model.dto.topico.GuardarTopicoDTO;
import com.bonidev.foro_hub.model.dto.topico.MostrarTopicoDTO;
import com.bonidev.foro_hub.model.entity.TopicoEntity;
import com.bonidev.foro_hub.model.enums.StatusEnum;
import com.bonidev.foro_hub.repository.CursoRepository;
import com.bonidev.foro_hub.security.repository.UsuarioRepository;
import com.bonidev.foro_hub.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/topicos")
public class TopicoController {
    private final TopicoService service;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    @Autowired
    public TopicoController(TopicoService service, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @PostMapping
    public ResponseEntity<?> guardarTopico(@RequestBody @Valid GuardarTopicoDTO dto) {

        if (dto.toString().isEmpty()) {
            return ResponseEntity.badRequest().body("El tópico debe contener datos.");
        }

        TopicoEntity topico = new TopicoEntity(dto.titulo(), dto.mensaje(), LocalDateTime.now(), StatusEnum.EN_PROGRESO, dto.autor(), dto.curso());
        usuarioRepository.save(dto.autor());
        cursoRepository.save(dto.curso());
        service.save(topico);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(location).body("El tópico fue almacenado satisfactoriamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MostrarTopicoDTO> mostrarTopico(@PathVariable Long id) {
        return ResponseEntity.ok(service.mostrar(id));
    }

    @GetMapping
    public ResponseEntity<Page<GuardarTopicoDTO>> mostrarTopicos(@PageableDefault(size = 5) Pageable paginacion) {
        return ResponseEntity.ok(service.mostrarTodosLosMedicos(paginacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MostrarTopicoDTO> actualizarTopico(@PathVariable Long id, @RequestBody @Valid GuardarTopicoDTO dto) {
        MostrarTopicoDTO topicoActualizado = service.actualizarTopico(id, dto);

        return ResponseEntity.ok(topicoActualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTopico(@PathVariable Long id) {
        try {
            service.eliminarTopico(id);
            return ResponseEntity.ok("Elemento con id: " + id + " eliminado satisfactoriamente.");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al eliminar el tópico.");
        }
    }

}
