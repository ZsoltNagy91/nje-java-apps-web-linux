package com.nje_web.beadando.dto;

import java.time.LocalDate;

public record LabdarugoApiResponse(
        Integer id,
        Integer mezszam,
        String vezeteknev,
        String utonev,
        String teljesNev,
        Integer klubId,
        String klub,
        Integer posztId,
        String poszt,
        LocalDate szulido,
        boolean magyar,
        boolean kulfoldi,
        Integer ertek) {
}
