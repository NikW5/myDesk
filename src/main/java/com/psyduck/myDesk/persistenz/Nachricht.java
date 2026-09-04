package com.psyduck.myDesk.persistenz;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nachricht")
public class Nachricht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "absender_id", nullable = false)
    private Benutzer absender;

    @ManyToOne
    @JoinColumn(name = "empfaenger_id", nullable = false)
    private Benutzer empfaenger;

    @Column(nullable = false)
    private String titel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String inhalt;

    @Column(name = "empfangen_am", nullable = false)
    private LocalDateTime empfangenAm;

    protected Nachricht() {
    }

    public Nachricht(
            Benutzer absender,
            Benutzer empfaenger,
            String titel,
            String inhalt,
            LocalDateTime empfangenAm) {

        this.absender = absender;
        this.empfaenger = empfaenger;
        this.titel = titel;
        this.inhalt = inhalt;
        this.empfangenAm = empfangenAm;
    }

    public Integer getId() {
        return id;
    }

    public Benutzer getAbsender() {
        return absender;
    }

    public Benutzer getEmpfaenger() {
        return empfaenger;
    }

    public String getTitel() {
        return titel;
    }

    public String getInhalt() {
        return inhalt;
    }

    public LocalDateTime getEmpfangenAm() {
        return empfangenAm;
    }

    public String getBenutzer() {
        return absender.getName();
    }

    public String getVorschau() {
        if (inhalt == null) {
            return "";
        }

        if (inhalt.length() <= 50) {
            return inhalt;
        }

        return inhalt.substring(0, 50) + "...";
    }
}
