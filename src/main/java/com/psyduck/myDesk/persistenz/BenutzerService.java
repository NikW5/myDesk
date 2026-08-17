package com.psyduck.myDesk.persistenz;

import java.util.ArrayList;
import java.util.List;

public class BenutzerService {

    private static final List<Benutzer> BENUTZER = new ArrayList<>();

    static {
        BENUTZER.add(new Benutzer(
                "i-aah@test.de",
                "1234",
                "I-Aah"));

        BENUTZER.add(new Benutzer(
                "enton@test.de",
                "abcd",
                "Enton"));
        
        BENUTZER.add(new Benutzer(
                "diddi@test.de",
                "5678",
                "Diddi"));

        BENUTZER.add(new Benutzer(
                "gans@test.de",
                "efgh",
                "Gans"));
        
        BENUTZER.add(new Benutzer(
                "schneemann@test.de",
                "4321",
                "Schneemann"));
    }

    public static boolean anmelden(String emailOderBenutzername, String passwort) {
        return BENUTZER.stream()
                .anyMatch(benutzer ->
                        (benutzer.getEmail().equalsIgnoreCase(emailOderBenutzername)
                        || benutzer.getName().equalsIgnoreCase(emailOderBenutzername))
                        && benutzer.getPasswort().equals(passwort));
    }
    
    public static List<Benutzer> getBenutzer() {
        return BENUTZER;
    }
}