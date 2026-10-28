package com.nje_web.beadando.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class LabdarugoForm {

    @NotNull(message = "A mezszám megadása kötelező.")
    @Min(value = 0, message = "A mezszám nem lehet negatív.")
    private Integer mezszam;

    @NotNull(message = "Válassz klubot.")
    private Integer klubId;

    @NotNull(message = "Válassz posztot.")
    private Integer posztId;

    @Size(max = 50, message = "Az utónév legfeljebb 50 karakter lehet.")
    private String utonev;

    @NotBlank(message = "A vezetéknév megadása kötelező.")
    @Size(max = 50, message = "A vezetéknév legfeljebb 50 karakter lehet.")
    private String vezeteknev;

    @NotNull(message = "A születési dátum megadása kötelező.")
    @Past(message = "A születési dátum csak múltbeli dátum lehet.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate szulido;

    private boolean magyar;
    private boolean kulfoldi;

    @NotNull(message = "Az érték megadása kötelező.")
    @Min(value = 0, message = "Az érték nem lehet negatív.")
    private Integer ertek;

    public Integer getMezszam() { return mezszam; }
    public void setMezszam(Integer mezszam) { this.mezszam = mezszam; }
    public Integer getKlubId() { return klubId; }
    public void setKlubId(Integer klubId) { this.klubId = klubId; }
    public Integer getPosztId() { return posztId; }
    public void setPosztId(Integer posztId) { this.posztId = posztId; }
    public String getUtonev() { return utonev; }
    public void setUtonev(String utonev) { this.utonev = utonev; }
    public String getVezeteknev() { return vezeteknev; }
    public void setVezeteknev(String vezeteknev) { this.vezeteknev = vezeteknev; }
    public LocalDate getSzulido() { return szulido; }
    public void setSzulido(LocalDate szulido) { this.szulido = szulido; }
    public boolean isMagyar() { return magyar; }
    public void setMagyar(boolean magyar) { this.magyar = magyar; }
    public boolean isKulfoldi() { return kulfoldi; }
    public void setKulfoldi(boolean kulfoldi) { this.kulfoldi = kulfoldi; }
    public Integer getErtek() { return ertek; }
    public void setErtek(Integer ertek) { this.ertek = ertek; }
}
