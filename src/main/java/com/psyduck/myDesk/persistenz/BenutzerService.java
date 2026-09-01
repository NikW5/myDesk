package com.psyduck.myDesk.persistenz;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class BenutzerService {

private final BenutzerRepository benutzerRepository;
private final PasswordEncoder passwordEncoder;

public BenutzerService(BenutzerRepository benutzerRepository) {
    this.benutzerRepository = benutzerRepository;
    this.passwordEncoder = new BCryptPasswordEncoder();
}


public boolean anmelden(String emailOderBenutzername, String passwort) {

    Optional<Benutzer> benutzer =
            benutzerRepository.findByEmailIgnoreCase(emailOderBenutzername);

    if (benutzer.isEmpty()) {
        benutzer = benutzerRepository
                .findByNameIgnoreCase(emailOderBenutzername);
    }

    return benutzer
            .map(b -> passwordEncoder.matches(
                    passwort,
                    b.getPasswort()
            ))
            .orElse(false);
}


public List<Benutzer> getBenutzer() {
    return benutzerRepository.findAll();
}

public Optional<Benutzer> findeNachEmail(String email) {
    return benutzerRepository.findByEmailIgnoreCase(email);
}

public Benutzer speichern(Benutzer benutzer) {

    String passwort = benutzer.getPasswort();

    String passwortHash = passwordEncoder.encode(passwort);

    benutzer.setPasswort(passwortHash);

    return benutzerRepository.save(benutzer);
}

}