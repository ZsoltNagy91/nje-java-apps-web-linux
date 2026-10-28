package com.nje_web.beadando.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LabdarugoApiRequest(
        @NotNull(message = "A mezszám megadása kötelező.")
        @Min(value = 0, message = "A mezszám nem lehet negatív.")
        Integer mezszam,
        @NotNull(message = "A klub azonosítója kötelező.")
        Integer klubId,
        @NotNull(message = "A poszt azonosítója kötelező.")
        Integer posztId,
        @Size(max = 50, message = "Az utónév legfeljebb 50 karakter lehet.")
        String utonev,
        @NotBlank(message = "A vezetéknév megadása kötelező.")
        @Size(max = 50, message = "A vezetéknév legfeljebb 50 karakter lehet.")
        String vezeteknev,
        @NotNull(message = "A születési dátum megadása kötelező.")
        @Past(message = "A születési dátum csak múltbeli dátum lehet.")
        LocalDate szulido,
        boolean magyar,
        boolean kulfoldi,
        @NotNull(message = "Az érték megadása kötelező.")
        @Min(value = 0, message = "Az érték nem lehet negatív.")
        Integer ertek) {
}
