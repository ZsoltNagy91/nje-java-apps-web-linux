package com.nje_web.beadando.repository;

import com.nje_web.beadando.model.Labdarugo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LabdarugoRepository extends JpaRepository<Labdarugo, Integer> {

    @Query("select l from Labdarugo l join fetch l.klub join fetch l.poszt " +
            "order by l.klub.csapatnev, l.vezeteknev, l.utonev")
    List<Labdarugo> findAllWithKlubAndPoszt();

    @Query("select l from Labdarugo l join fetch l.klub join fetch l.poszt " +
            "where l.klub.id = :klubId order by l.vezeteknev, l.utonev")
    List<Labdarugo> findByKlubIdWithKlubAndPoszt(@Param("klubId") Integer klubId);
}
