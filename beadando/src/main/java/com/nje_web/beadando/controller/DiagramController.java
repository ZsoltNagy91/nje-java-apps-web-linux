package com.nje_web.beadando.controller;

import com.nje_web.beadando.repository.LabdarugoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DiagramController {

    private final LabdarugoRepository labdarugoRepository;

    public DiagramController(LabdarugoRepository labdarugoRepository) {
        this.labdarugoRepository = labdarugoRepository;
    }

    @GetMapping("/diagram")
    public String diagram(Model model) {
        var klubErtekek = labdarugoRepository.osszesErtekKlubonkent();
        var posztEloszlas = labdarugoRepository.letszamPosztonkent();
        var klubNemzetisegek = labdarugoRepository.nemzetisegLetszamKlubonkent();

        model.addAttribute("klubErtekNevek", klubErtekek.stream().map(stat -> stat.klubNev()).toList());
        model.addAttribute("klubErtekAdatok", klubErtekek.stream().map(stat -> stat.osszErtek()).toList());
        model.addAttribute("posztNevek", posztEloszlas.stream().map(stat -> stat.posztNev()).toList());
        model.addAttribute("posztLetszamok", posztEloszlas.stream().map(stat -> stat.letszam()).toList());
        model.addAttribute("nemzetisegKlubNevek", klubNemzetisegek.stream().map(stat -> stat.klubNev()).toList());
        model.addAttribute("magyarLetszamok", klubNemzetisegek.stream().map(stat -> stat.magyarLetszam()).toList());
        model.addAttribute("kulfoldiLetszamok", klubNemzetisegek.stream().map(stat -> stat.kulfoldiLetszam()).toList());
        return "diagram";
    }
}
