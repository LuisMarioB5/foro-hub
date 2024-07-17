package com.bonidev.foro_hub.repository;

import com.bonidev.foro_hub.model.entity.TopicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<TopicoEntity, Long> {
}
