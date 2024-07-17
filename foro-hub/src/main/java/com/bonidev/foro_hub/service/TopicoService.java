package com.bonidev.foro_hub.service;

import com.bonidev.foro_hub.errors.ResourceNotFoundException;
import com.bonidev.foro_hub.model.dto.topico.GuardarTopicoDTO;
import com.bonidev.foro_hub.model.dto.topico.MostrarTopicoDTO;
import com.bonidev.foro_hub.model.entity.CursoEntity;
import com.bonidev.foro_hub.model.entity.TopicoEntity;
import com.bonidev.foro_hub.model.entity.UsuarioEntity;
import com.bonidev.foro_hub.repository.CursoRepository;
import com.bonidev.foro_hub.repository.TopicoRepository;
import com.bonidev.foro_hub.security.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TopicoService {
    private final TopicoRepository repository;
    private final UsuarioRepository userrepository;
    private final CursoRepository cursoRepository;

    @Autowired
    public TopicoService(TopicoRepository repository, UsuarioRepository userrepository, CursoRepository cursoRepository) {
        this.repository = repository;
        this.userrepository = userrepository;
        this.cursoRepository = cursoRepository;
    }

    public void save (TopicoEntity topico) {
        repository.save(topico);
    }

    public TopicoEntity getReferenceById(Long id) {
        return repository.getReferenceById(id);
    }

    public MostrarTopicoDTO mostrar(Long id) {
        TopicoEntity topicoEntity = repository.getReferenceById(id);

        return new MostrarTopicoDTO(
                topicoEntity.getTitulo(),
                topicoEntity.getMensaje(),
                topicoEntity.getFechaCreacion(),
                topicoEntity.getStatus().capitalize(),
                topicoEntity.getAutor(),
                topicoEntity.getCurso());
    }

    public Page<GuardarTopicoDTO> mostrarTodosLosMedicos(Pageable paginacion) {
        List<TopicoEntity> topicosEntity = repository.findAll();
        List<GuardarTopicoDTO> topicos = topicosEntity.stream()
                .map(topicoEntity -> new GuardarTopicoDTO(
                        topicoEntity.getTitulo(),
                        topicoEntity.getMensaje(),
                        topicoEntity.getAutor(),
                        topicoEntity.getCurso()))
                .toList();

        return new PageImpl<GuardarTopicoDTO>(topicos);
    }

    public MostrarTopicoDTO actualizarTopico(Long id, GuardarTopicoDTO guardarTopicoDTO) {
        TopicoEntity topico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico no encontrado con id: " + id));

        topico.setTitulo(guardarTopicoDTO.titulo());
        topico.setMensaje(guardarTopicoDTO.mensaje());

        if (!topico.getAutor().getId().equals(guardarTopicoDTO.autor().getId())) {
            UsuarioEntity nuevoAutor = userrepository.findById(guardarTopicoDTO.autor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + guardarTopicoDTO.autor().getId()));
            topico.setAutor(nuevoAutor);
        }

        if (!topico.getCurso().getId().equals(guardarTopicoDTO.curso().getId())) {
            CursoEntity nuevoCurso = cursoRepository.findById(guardarTopicoDTO.curso().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + guardarTopicoDTO.curso().getId()));
            topico.setCurso(nuevoCurso);
        }

        if (!topico.getAutor().equals(guardarTopicoDTO.autor())) {
            userrepository.save(topico.getAutor());
        }
        if (!topico.getCurso().equals(guardarTopicoDTO.curso())) {
            cursoRepository.save(topico.getCurso());
        }

        topico = repository.save(topico);

        return new MostrarTopicoDTO(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus().capitalize(),
                topico.getAutor(),
                topico.getCurso()
        );
    }

    public void eliminarTopico(Long id) {
        TopicoEntity topico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tópico no encontrado con id: " + id));

        repository.deleteById(id);
    }
}
