package com.psyduck.myDesk.persistenz;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ToDoRepository toDoRepository;
    private final NachrichtRepository nachrichtRepository;

    public BenutzerService(BenutzerRepository benutzerRepository, PasswordEncoder passwordEncoder, ToDoRepository toDoRepository, NachrichtRepository nachrichtRepository) {
        this.benutzerRepository = benutzerRepository;
        this.passwordEncoder = passwordEncoder;
        this.toDoRepository = toDoRepository;
        this.nachrichtRepository = nachrichtRepository;
    }

    public Optional<Benutzer> anmelden(String benutzername, String passwort) {
        Optional<Benutzer> benutzer = benutzerRepository.findByNameIgnoreCase(benutzername);
        return benutzer.filter(b -> passwordEncoder.matches(passwort, b.getPasswort()));
    }

    public Optional<Benutzer> findeNachBenutzername(String name) {
        return benutzerRepository.findByNameIgnoreCase(name);
    }

    public Optional<Benutzer> findeNachEmail(String email) {
        return benutzerRepository.findByEmailIgnoreCase(email);
    }

    public List<Benutzer> getBenutzer() {
        return benutzerRepository.findAll();
    }

    public Benutzer speichern(Benutzer benutzer) {
        benutzer.setPasswort(passwordEncoder.encode(benutzer.getPasswort()));
        return benutzerRepository.save(benutzer);
    }

    @Transactional
    public Benutzer aktualisiereProfil(Benutzer benutzer, String benutzername, String email, String vorname, String nachname) {
        benutzername = benutzername == null ? "" : benutzername.trim();
        email = email == null ? "" : email.trim();

        if (benutzername.isBlank() || email.isBlank()) {
            throw new IllegalArgumentException("Benutzername und E-Mail dürfen nicht leer sein.");
        }

        String finalBenutzername = benutzername;
        String finalEmail = email;

        benutzerRepository.findByNameIgnoreCase(finalBenutzername)
                .filter(b -> !b.getId().equals(benutzer.getId()))
                .ifPresent(b -> { throw new IllegalArgumentException("Dieser Benutzername ist bereits vergeben."); });

        benutzerRepository.findByEmailIgnoreCase(finalEmail)
                .filter(b -> !b.getId().equals(benutzer.getId()))
                .ifPresent(b -> { throw new IllegalArgumentException("Diese E-Mail-Adresse ist bereits registriert."); });

        benutzer.setName(finalBenutzername);
        benutzer.setEmail(finalEmail);
        benutzer.setVorname(leererWertZuNull(vorname));
        benutzer.setNachname(leererWertZuNull(nachname));

        return benutzerRepository.save(benutzer);
    }

    @Transactional
    public void aenderePasswort(Benutzer benutzer, String aktuellesPasswort, String neuesPasswort) {
        if (!passwordEncoder.matches(aktuellesPasswort, benutzer.getPasswort())) {
            throw new IllegalArgumentException("Das aktuelle Passwort ist falsch.");
        }

        if (neuesPasswort == null || neuesPasswort.isBlank()) {
            throw new IllegalArgumentException("Das neue Passwort darf nicht leer sein.");
        }

        benutzer.setPasswort(passwordEncoder.encode(neuesPasswort));
        benutzerRepository.save(benutzer);
    }

    @Transactional
    public void loescheKonto(Benutzer benutzer, String aktuellesPasswort) {
        if (!passwordEncoder.matches(aktuellesPasswort, benutzer.getPasswort())) {
            throw new IllegalArgumentException("Das aktuelle Passwort ist falsch.");
        }

        Integer benutzerId = benutzer.getId();
        nachrichtRepository.setAbsenderNull(benutzerId);
        nachrichtRepository.setEmpfaengerNull(benutzerId);
        toDoRepository.deleteByBenutzer(benutzer);
        benutzerRepository.delete(benutzer);
    }

    private String leererWertZuNull(String wert) {
        return wert == null || wert.isBlank() ? null : wert.trim();
    }
}