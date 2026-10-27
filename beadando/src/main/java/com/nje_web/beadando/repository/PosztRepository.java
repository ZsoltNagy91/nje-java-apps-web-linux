package com.nje_web.beadando.repository;

import com.nje_web.beadando.model.Poszt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosztRepository extends JpaRepository<Poszt, Integer> {
    List<Poszt> findAllByOrderByNevAsc();
}
