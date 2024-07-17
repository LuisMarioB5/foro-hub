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
import java.util.Objects;

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

    @Transactional
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

        // Actualizar título y mensaje del tópico
        topico.setTitulo(guardarTopicoDTO.titulo());
        topico.setMensaje(guardarTopicoDTO.mensaje());

        // Verificar y actualizar el autor si es necesario
        UsuarioEntity nuevoAutor = guardarTopicoDTO.autor();
        if (nuevoAutor != null && nuevoAutor.getId() != null) {
            System.out.println("hola");
            UsuarioEntity autorPersistido = userrepository.findById(nuevoAutor.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Autor (usuario) no encontrado con id: " + nuevoAutor.getId()));

            if (!Objects.equals(autorPersistido.getNombre(), nuevoAutor.getNombre())
                    || !Objects.equals(autorPersistido.getEmail(), nuevoAutor.getEmail())
                    || !Objects.equals(autorPersistido.getPassword(), nuevoAutor.getPassword())) {
                autorPersistido.setNombre(nuevoAutor.getNombre());
                autorPersistido.setEmail(nuevoAutor.getEmail());
                autorPersistido.setPassword(nuevoAutor.getPassword());

                userrepository.save(autorPersistido);
            }

            // Actualizar el autor del tópico
            topico.setAutor(autorPersistido);
        }

        // Verificar y actualizar el curso si es necesario
        CursoEntity nuevoCurso = guardarTopicoDTO.curso();
        if (nuevoCurso != null && nuevoCurso.getId() != null) {
            CursoEntity cursoPersistido = cursoRepository.findById(nuevoCurso.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + nuevoCurso.getId()));

            // Comparar propiedades relevantes y actualizar si es necesario
            if (!Objects.equals(cursoPersistido.getNombre(), nuevoCurso.getNombre())
                    || !Objects.equals(cursoPersistido.getCategoria(), nuevoCurso.getCategoria())) {
                cursoPersistido.setNombre(nuevoCurso.getNombre());
                cursoPersistido.setCategoria(nuevoCurso.getCategoria());

                cursoRepository.save(cursoPersistido);
            }

            // Actualizar el curso del tópico
            topico.setCurso(cursoPersistido);
        }

        // Guardar los cambios en el tópico
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
