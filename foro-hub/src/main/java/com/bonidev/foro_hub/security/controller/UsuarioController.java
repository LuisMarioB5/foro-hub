package com.bonidev.foro_hub.security.controller;

import com.bonidev.foro_hub.security.dto.AutenticacionDTO;
import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import com.bonidev.foro_hub.security.dto.UsuarioDTO;
import com.bonidev.foro_hub.security.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody @Valid AutenticacionDTO user) {
        UsuarioEntity userEntity = usuarioService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userEntity.getId())
                .toUri();
        return ResponseEntity.created(location).body("El usuario fue almacenado satisfactoriamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> showUser(@PathVariable Long id) {
        UsuarioEntity user = usuarioService.findById(id);
        return ResponseEntity.ok(new UsuarioDTO(user));
    }
}
