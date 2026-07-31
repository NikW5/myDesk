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
    }

    public static boolean anmelden(String email, String passwort) {
        return BENUTZER.stream()
                .anyMatch(benutzer ->
                        benutzer.getEmail().equalsIgnoreCase(email)
                                && benutzer.getPasswort().equals(passwort));
    }
}