package com.psyduck.myDesk.persistenz;

import org.springframework.stereotype.Service;

@Service
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;

    public BenutzerService(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    public Benutzer findByName(String username) {
        return benutzerRepository.findByName(username)
                .orElse(null);
    }
}

