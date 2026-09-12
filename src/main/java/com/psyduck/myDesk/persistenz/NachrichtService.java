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

    public boolean hatNeueNachricht(Benutzer empfaenger) {
        return nachrichtRepository
                .countByEmpfaengerAndGelesenFalse(empfaenger) > 0;
    }

    public void alsGelesenMarkieren(Nachricht nachricht) {
        nachricht.setGelesen(true);
        nachrichtRepository.save(nachricht);
    }

    public Nachricht speichern(
            Benutzer absender,
            Benutzer empfaenger,
            String titel,
            String inhalt) {

        Nachricht nachricht = new Nachricht(
                absender,
                empfaenger,
                titel,
                inhalt,
                LocalDateTime.now()
        );

        return nachrichtRepository.save(nachricht);
    }
    
    public long getAnzahlUngeleseneNachrichten(Benutzer empfaenger) {
        return nachrichtRepository
                .countByEmpfaengerAndGelesenFalse(empfaenger);
    }

}
