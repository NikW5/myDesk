package com.psyduck.myDesk.persistenz;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NachrichtService {

    private final NachrichtRepository nachrichtRepository;

    public NachrichtService(NachrichtRepository nachrichtRepository) {
        this.nachrichtRepository = nachrichtRepository;
    }

    public List<Nachricht> getNachrichten(Benutzer empfaenger) {
        return nachrichtRepository.findByEmpfaenger(empfaenger);
    }
    
    public long getAnzahlNachrichten(Benutzer empfaenger) {
        return nachrichtRepository.countByEmpfaenger(empfaenger);
    }

    public Nachricht speichern(Benutzer absender, Benutzer empfaenger, String titel, String inhalt) {

        Nachricht nachricht = new Nachricht(absender, empfaenger, titel, inhalt, LocalDateTime.now()
        );

        return nachrichtRepository.save(nachricht);
    }
}
