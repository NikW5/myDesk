
package com.psyduck.myDesk.persistenz;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psyduck.myDesk.security.AktuellerBenutzerService;

@Service
@Transactional
public class KalendereintragService {

    private final KalendereintragRepository repository;
    private final AktuellerBenutzerService aktuellerBenutzerService;

    public KalendereintragService(
            KalendereintragRepository repository,
            AktuellerBenutzerService aktuellerBenutzerService) {

        this.repository = repository;
        this.aktuellerBenutzerService = aktuellerBenutzerService;
    }

    public List<Kalendereintrag> getEigeneEintraege() {
        Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) return List.of();

        return repository.findByBenutzer(benutzer);
    }

    public Kalendereintrag getEigenerEintrag(Integer id) {
        return findeEigenenEintrag(id);
    }

    public Kalendereintrag speichern(Kalendereintrag eintrag) {
        Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) {
            throw new IllegalStateException("Kein angemeldeter Benutzer vorhanden.");
        }

        pruefeEintrag(eintrag.getTitel(), eintrag.getDatum(), eintrag.getStartzeit(), eintrag.getEndzeit());

        eintrag.setBenutzer(benutzer);

        return repository.save(eintrag);
    }

    public Kalendereintrag aktualisieren(
            Integer id,
            String titel,
            String beschreibung,
            LocalDate datum,
            LocalTime startzeit,
            LocalTime endzeit) {

        Kalendereintrag eintrag = findeEigenenEintrag(id);

        pruefeEintrag(titel, datum, startzeit, endzeit);

        eintrag.setTitel(titel.trim());
        eintrag.setBeschreibung(beschreibung == null || beschreibung.isBlank() ? null : beschreibung.trim());
        eintrag.setDatum(datum);
        eintrag.setStartzeit(startzeit);
        eintrag.setEndzeit(endzeit);

        return repository.save(eintrag);
    }

    public void loeschen(Integer id) {
        Kalendereintrag eintrag = findeEigenenEintrag(id);
        repository.delete(eintrag);
    }

    private Kalendereintrag findeEigenenEintrag(Integer id) {
        Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) {
            throw new IllegalStateException("Kein angemeldeter Benutzer vorhanden.");
        }

        return repository.findByIdAndBenutzer(id, benutzer)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Kalendereintrag wurde nicht gefunden."));
    }

    private void pruefeEintrag(String titel, LocalDate datum, LocalTime startzeit, LocalTime endzeit) {
        if (titel == null || titel.isBlank()) {
            throw new IllegalArgumentException("Der Titel darf nicht leer sein.");
        }

        if (datum == null) {
            throw new IllegalArgumentException("Das Datum muss angegeben werden.");
        }

        if ((startzeit == null) != (endzeit == null)) {
            throw new IllegalArgumentException("Start- und Endzeit müssen gemeinsam angegeben werden.");
        }

        if (startzeit != null && !endzeit.isAfter(startzeit)) {
            throw new IllegalArgumentException("Die Endzeit muss nach der Startzeit liegen.");
        }
    }
}
