package com.psyduck.myDesk.persistenz;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    
    @OneToMany(
    	    mappedBy = "nachricht",
    	    cascade = CascadeType.ALL,
    	    orphanRemoval = true,
    	    fetch = FetchType.EAGER
    	)
    	private List<Anhang> anhaenge = new ArrayList<>();

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
    
    public List<Anhang> getAnhaenge() {
        return anhaenge;
    }

    public void addAnhang(Anhang anhang) {
        anhaenge.add(anhang);
        anhang.setNachricht(this);
    }

    public void removeAnhang(Anhang anhang) {
        anhaenge.remove(anhang);
        anhang.setNachricht(null);
    }
}
