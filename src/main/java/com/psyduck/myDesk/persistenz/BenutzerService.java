package com.psyduck.myDesk.persistenz;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;
    private final PasswordEncoder passwordEncoder;


    public BenutzerService(BenutzerRepository benutzerRepository, PasswordEncoder passwordEncoder) {

        this.benutzerRepository = benutzerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<Benutzer> anmelden(String benutzername, String passwort) {

        Optional<Benutzer> benutzer =
                benutzerRepository.findByNameIgnoreCase(benutzername);

        if (benutzer.isPresent() && passwordEncoder.matches(passwort, benutzer.get().getPasswort())) {
            return benutzer;
        }

        return Optional.empty();
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
}
