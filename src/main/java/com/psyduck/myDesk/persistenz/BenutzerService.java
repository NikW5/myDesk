package com.psyduck.myDesk.persistenz;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class BenutzerService {

private final BenutzerRepository benutzerRepository;

public BenutzerService(BenutzerRepository benutzerRepository) {
    this.benutzerRepository = benutzerRepository;
}

public boolean anmelden(String emailOderBenutzername, String passwort) {

    Optional<Benutzer> benutzer = benutzerRepository
            .findByEmailIgnoreCase(emailOderBenutzername);

    if (benutzer.isEmpty()) {
        benutzer = benutzerRepository
                .findByNameIgnoreCase(emailOderBenutzername);
    }

    return benutzer
            .map(b -> b.getPasswort().equals(passwort))
            .orElse(false);
}

public List<Benutzer> getBenutzer() {
    return benutzerRepository.findAll();
}

public Optional<Benutzer> findeNachEmail(String email) {
    return benutzerRepository.findByEmailIgnoreCase(email);
}

public Benutzer speichern(Benutzer benutzer) {
    return benutzerRepository.save(benutzer);
}


}