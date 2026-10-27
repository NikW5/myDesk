package com.psyduck.myDesk.persistenz;

public class BenutzerSession {

    private static Benutzer aktuellerBenutzer;

    public static Benutzer getAktuellerBenutzer() {
        return aktuellerBenutzer;
    }

    public static void setAktuellerBenutzer(Benutzer benutzer) {
        aktuellerBenutzer = benutzer;
    }

    public static void abmelden() {
        aktuellerBenutzer = null;
    }
}
