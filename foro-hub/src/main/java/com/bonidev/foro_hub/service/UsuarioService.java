package com.bonidev.foro_hub.service;

import com.bonidev.foro_hub.security.dto.AutenticacionDTO;
import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import com.bonidev.foro_hub.security.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public UsuarioEntity save(AutenticacionDTO dto) {
        return repository.save(new UsuarioEntity(dto.login(), dto.clave()));
    }

    public UsuarioEntity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con el id: " + id + " no fue encontrado..."));
    }
}
