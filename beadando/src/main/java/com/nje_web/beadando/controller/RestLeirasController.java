package com.nje_web.beadando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestLeirasController {

    @GetMapping("/rest")
    public String rest() {
        return "rest";
    }
}
