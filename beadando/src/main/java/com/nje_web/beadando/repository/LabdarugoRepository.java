package com.nje_web.beadando.repository;

import com.nje_web.beadando.dto.KlubErtekOsszesito;
import com.nje_web.beadando.dto.KlubNemzetisegOsszesito;
import com.nje_web.beadando.dto.PosztEloszlas;
import com.nje_web.beadando.model.Labdarugo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LabdarugoRepository extends JpaRepository<Labdarugo, Integer> {

    @Query("select l from Labdarugo l join fetch l.klub join fetch l.poszt " +
            "order by l.klub.csapatnev, l.vezeteknev, l.utonev")
    List<Labdarugo> findAllWithKlubAndPoszt();

    @Query("select l from Labdarugo l join fetch l.klub join fetch l.poszt " +
            "where l.klub.id = :klubId order by l.vezeteknev, l.utonev")
    List<Labdarugo> findByKlubIdWithKlubAndPoszt(@Param("klubId") Integer klubId);

    @Query("select new com.nje_web.beadando.dto.KlubErtekOsszesito(l.klub.csapatnev, sum(l.ertek)) " +
            "from Labdarugo l group by l.klub.id, l.klub.csapatnev order by l.klub.csapatnev")
    List<KlubErtekOsszesito> osszesErtekKlubonkent();

    @Query("select new com.nje_web.beadando.dto.PosztEloszlas(l.poszt.nev, count(l)) " +
            "from Labdarugo l group by l.poszt.id, l.poszt.nev order by l.poszt.nev")
    List<PosztEloszlas> letszamPosztonkent();

    @Query("select new com.nje_web.beadando.dto.KlubNemzetisegOsszesito(l.klub.csapatnev, " +
            "sum(case when l.magyar = true then 1 else 0 end), " +
            "sum(case when l.kulfoldi = true then 1 else 0 end)) " +
            "from Labdarugo l group by l.klub.id, l.klub.csapatnev order by l.klub.csapatnev")
    List<KlubNemzetisegOsszesito> nemzetisegLetszamKlubonkent();
}
