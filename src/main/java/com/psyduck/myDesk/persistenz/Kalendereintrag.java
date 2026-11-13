package com.psyduck.myDesk.persistenz;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
@Table(name = "kalendereintrag")
public class Kalendereintrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titel;

    private String beschreibung;

    @Column(nullable = false)
    private LocalDate datum;

    private LocalTime startzeit;

    private LocalTime endzeit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "benutzer_id", nullable = false)
    private Benutzer benutzer;

    protected Kalendereintrag() {
    }

    public Kalendereintrag(String titel, LocalDate datum) {
        this.titel = titel;
        this.datum = datum;
    }

    public Integer getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public LocalTime getStartzeit() {
        return startzeit;
    }

    public void setStartzeit(LocalTime startzeit) {
        this.startzeit = startzeit;
    }

    public LocalTime getEndzeit() {
        return endzeit;
    }

    public void setEndzeit(LocalTime endzeit) {
        this.endzeit = endzeit;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }
}