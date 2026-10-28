package com.nje_web.beadando.controller;

import com.nje_web.beadando.dto.LabdarugoForm;
import com.nje_web.beadando.model.Klub;
import com.nje_web.beadando.model.Labdarugo;
import com.nje_web.beadando.model.Poszt;
import com.nje_web.beadando.repository.KlubRepository;
import com.nje_web.beadando.repository.LabdarugoRepository;
import com.nje_web.beadando.repository.PosztRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CrudController {

    private final LabdarugoRepository labdarugoRepository;
    private final KlubRepository klubRepository;
    private final PosztRepository posztRepository;

    public CrudController(LabdarugoRepository labdarugoRepository,
                          KlubRepository klubRepository,
                          PosztRepository posztRepository) {
        this.labdarugoRepository = labdarugoRepository;
        this.klubRepository = klubRepository;
        this.posztRepository = posztRepository;
    }

    @GetMapping("/crud")
    public String crud(Model model) {
        model.addAttribute("labdarugok", labdarugoRepository.findAllWithKlubAndPoszt());
        model.addAttribute("klubok", klubRepository.findAllByOrderByCsapatnevAsc());
        model.addAttribute("posztok", posztRepository.findAllByOrderByNevAsc());
        if (!model.containsAttribute("labdarugoForm")) {
            model.addAttribute("labdarugoForm", new LabdarugoForm());
        }
        return "crud";
    }

    @PostMapping("/crud")
    public String ujLabdarugo(@Valid @ModelAttribute("labdarugoForm") LabdarugoForm form,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        var klub = form.getKlubId() == null ? null : klubRepository.findById(form.getKlubId()).orElse(null);
        var poszt = form.getPosztId() == null ? null : posztRepository.findById(form.getPosztId()).orElse(null);

        if (form.getKlubId() != null && klub == null) {
            bindingResult.rejectValue("klubId", "klub.invalid", "A kiválasztott klub nem létezik.");
        }
        if (form.getPosztId() != null && poszt == null) {
            bindingResult.rejectValue("posztId", "poszt.invalid", "A kiválasztott poszt nem létezik.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("labdarugok", labdarugoRepository.findAllWithKlubAndPoszt());
            model.addAttribute("klubok", klubRepository.findAllByOrderByCsapatnevAsc());
            model.addAttribute("posztok", posztRepository.findAllByOrderByNevAsc());
            return "crud";
        }

        Labdarugo labdarugo = new Labdarugo();
        beallitAdatokat(labdarugo, form, klub, poszt);
        labdarugoRepository.save(labdarugo);

        redirectAttributes.addFlashAttribute("sikerUzenet", "A labdarúgó felvétele sikerült.");
        return "redirect:/crud";
    }

    @GetMapping("/crud/{id}/szerkeszt")
    public String szerkesztes(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        var labdarugo = labdarugoRepository.findById(id);
        if (labdarugo.isEmpty()) {
            redirectAttributes.addFlashAttribute("hibaUzenet", "A keresett labdarúgó nem található.");
            return "redirect:/crud";
        }

        model.addAttribute("labdarugoId", id);
        model.addAttribute("labdarugoForm", formAdatok(labdarugo.get()));
        valasztokBetoltese(model);
        return "crud-szerkeszt";
    }

    @PostMapping("/crud/{id}/szerkeszt")
    public String mentesSzerkesztes(@PathVariable Integer id,
                                    @Valid @ModelAttribute("labdarugoForm") LabdarugoForm form,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        var letezoLabdarugo = labdarugoRepository.findById(id);
        if (letezoLabdarugo.isEmpty()) {
            redirectAttributes.addFlashAttribute("hibaUzenet", "A keresett labdarúgó nem található.");
            return "redirect:/crud";
        }

        var klub = form.getKlubId() == null ? null : klubRepository.findById(form.getKlubId()).orElse(null);
        var poszt = form.getPosztId() == null ? null : posztRepository.findById(form.getPosztId()).orElse(null);
        if (form.getKlubId() != null && klub == null) {
            bindingResult.rejectValue("klubId", "klub.invalid", "A kiválasztott klub nem létezik.");
        }
        if (form.getPosztId() != null && poszt == null) {
            bindingResult.rejectValue("posztId", "poszt.invalid", "A kiválasztott poszt nem létezik.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("labdarugoId", id);
            valasztokBetoltese(model);
            return "crud-szerkeszt";
        }

        beallitAdatokat(letezoLabdarugo.get(), form, klub, poszt);
        labdarugoRepository.save(letezoLabdarugo.get());
        redirectAttributes.addFlashAttribute("sikerUzenet", "A labdarúgó adatai frissültek.");
        return "redirect:/crud";
    }

    @GetMapping("/crud/{id}/torles")
    public String torlesMegerositese(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        var labdarugo = labdarugoRepository.findById(id);
        if (labdarugo.isEmpty()) {
            redirectAttributes.addFlashAttribute("hibaUzenet", "A keresett labdarúgó nem található.");
            return "redirect:/crud";
        }
        model.addAttribute("labdarugo", labdarugo.get());
        return "crud-torles-megerosites";
    }

    @PostMapping("/crud/{id}/torles")
    public String torles(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        var labdarugo = labdarugoRepository.findById(id);
        if (labdarugo.isEmpty()) {
            redirectAttributes.addFlashAttribute("hibaUzenet", "A keresett labdarúgó már nem található.");
        } else {
            labdarugoRepository.delete(labdarugo.get());
            redirectAttributes.addFlashAttribute("sikerUzenet", "A labdarúgó törlése sikerült.");
        }
        return "redirect:/crud";
    }

    private void valasztokBetoltese(Model model) {
        model.addAttribute("klubok", klubRepository.findAllByOrderByCsapatnevAsc());
        model.addAttribute("posztok", posztRepository.findAllByOrderByNevAsc());
    }

    private LabdarugoForm formAdatok(Labdarugo labdarugo) {
        LabdarugoForm form = new LabdarugoForm();
        form.setMezszam(labdarugo.getMezszam());
        form.setKlubId(labdarugo.getKlub().getId());
        form.setPosztId(labdarugo.getPoszt().getId());
        form.setUtonev(labdarugo.getUtonev());
        form.setVezeteknev(labdarugo.getVezeteknev());
        form.setSzulido(labdarugo.getSzulido());
        form.setMagyar(labdarugo.isMagyar());
        form.setKulfoldi(labdarugo.isKulfoldi());
        form.setErtek(labdarugo.getErtek());
        return form;
    }

    private void beallitAdatokat(Labdarugo labdarugo, LabdarugoForm form,
                                 Klub klub,
                                 Poszt poszt) {
        labdarugo.setMezszam(form.getMezszam());
        labdarugo.setKlub(klub);
        labdarugo.setPoszt(poszt);
        labdarugo.setUtonev(form.getUtonev() == null || form.getUtonev().isBlank() ? null : form.getUtonev().trim());
        labdarugo.setVezeteknev(form.getVezeteknev().trim());
        labdarugo.setSzulido(form.getSzulido());
        labdarugo.setMagyar(form.isMagyar());
        labdarugo.setKulfoldi(form.isKulfoldi());
        labdarugo.setErtek(form.getErtek());
    }
}
