package com.nje_web.beadando.controller;

import com.nje_web.beadando.dto.LabdarugoApiRequest;
import com.nje_web.beadando.dto.LabdarugoApiResponse;
import com.nje_web.beadando.model.Klub;
import com.nje_web.beadando.model.Labdarugo;
import com.nje_web.beadando.model.Poszt;
import com.nje_web.beadando.repository.KlubRepository;
import com.nje_web.beadando.repository.LabdarugoRepository;
import com.nje_web.beadando.repository.PosztRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/labdarugok")
public class LabdarugoApiController {

    private final LabdarugoRepository labdarugoRepository;
    private final KlubRepository klubRepository;
    private final PosztRepository posztRepository;

    public LabdarugoApiController(LabdarugoRepository labdarugoRepository,
                                  KlubRepository klubRepository,
                                  PosztRepository posztRepository) {
        this.labdarugoRepository = labdarugoRepository;
        this.klubRepository = klubRepository;
        this.posztRepository = posztRepository;
    }

    @GetMapping
    public List<LabdarugoApiResponse> lista() {
        return labdarugoRepository.findAllWithKlubAndPoszt().stream().map(this::valasz).toList();
    }

    @GetMapping("/{id}")
    public LabdarugoApiResponse egy(@PathVariable Integer id) {
        return valasz(keres(id));
    }

    @PostMapping
    public ResponseEntity<LabdarugoApiResponse> letrehozas(@Valid @RequestBody LabdarugoApiRequest request) {
        Labdarugo uj = new Labdarugo();
        beallit(uj, request);
        Labdarugo mentett = labdarugoRepository.save(uj);
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(mentett.getId()).toUri();
        return ResponseEntity.created(location).body(valasz(mentett));
    }

    @PutMapping("/{id}")
    public LabdarugoApiResponse modositas(@PathVariable Integer id,
                                          @Valid @RequestBody LabdarugoApiRequest request) {
        Labdarugo labdarugo = keres(id);
        beallit(labdarugo, request);
        return valasz(labdarugoRepository.save(labdarugo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> torles(@PathVariable Integer id) {
        labdarugoRepository.delete(keres(id));
        return ResponseEntity.noContent().build();
    }

    private Labdarugo keres(Integer id) {
        return labdarugoRepository.findWithKlubAndPosztById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "A labdarúgó nem található."));
    }

    private void beallit(Labdarugo labdarugo, LabdarugoApiRequest request) {
        Klub klub = klubRepository.findById(request.klubId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "A megadott klub nem létezik."));
        Poszt poszt = posztRepository.findById(request.posztId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "A megadott poszt nem létezik."));

        labdarugo.setMezszam(request.mezszam());
        labdarugo.setKlub(klub);
        labdarugo.setPoszt(poszt);
        labdarugo.setUtonev(request.utonev() == null || request.utonev().isBlank() ? null : request.utonev().trim());
        labdarugo.setVezeteknev(request.vezeteknev().trim());
        labdarugo.setSzulido(request.szulido());
        labdarugo.setMagyar(request.magyar());
        labdarugo.setKulfoldi(request.kulfoldi());
        labdarugo.setErtek(request.ertek());
    }

    private LabdarugoApiResponse valasz(Labdarugo labdarugo) {
        return new LabdarugoApiResponse(
                labdarugo.getId(), labdarugo.getMezszam(), labdarugo.getVezeteknev(),
                labdarugo.getUtonev(), labdarugo.getTeljesNev(),
                labdarugo.getKlub().getId(), labdarugo.getKlub().getCsapatnev(),
                labdarugo.getPoszt().getId(), labdarugo.getPoszt().getNev(),
                labdarugo.getSzulido(), labdarugo.isMagyar(), labdarugo.isKulfoldi(), labdarugo.getErtek());
    }
}
