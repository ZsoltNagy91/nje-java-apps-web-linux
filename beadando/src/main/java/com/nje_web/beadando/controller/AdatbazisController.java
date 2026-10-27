package com.nje_web.beadando.controller;

import com.nje_web.beadando.repository.KlubRepository;
import com.nje_web.beadando.repository.LabdarugoRepository;
import com.nje_web.beadando.repository.PosztRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdatbazisController {

    private final LabdarugoRepository labdarugoRepository;
    private final KlubRepository klubRepository;
    private final PosztRepository posztRepository;

    public AdatbazisController(LabdarugoRepository labdarugoRepository,
                               KlubRepository klubRepository,
                               PosztRepository posztRepository) {
        this.labdarugoRepository = labdarugoRepository;
        this.klubRepository = klubRepository;
        this.posztRepository = posztRepository;
    }

    @GetMapping("/adatbazis")
    public String adatbazis(@RequestParam(required = false) Integer klubId, Model model) {
        model.addAttribute("klubok", klubRepository.findAllByOrderByCsapatnevAsc());
        model.addAttribute("posztok", posztRepository.findAllByOrderByNevAsc());
        model.addAttribute("labdarugok", klubId == null
                ? labdarugoRepository.findAllWithKlubAndPoszt()
                : labdarugoRepository.findByKlubIdWithKlubAndPoszt(klubId));
        model.addAttribute("kivalasztottKlubId", klubId);
        return "adatbazis";
    }
}
