package com.nje_web.beadando.repository;

import com.nje_web.beadando.model.Klub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KlubRepository extends JpaRepository<Klub, Integer> {
    List<Klub> findAllByOrderByCsapatnevAsc();
}
