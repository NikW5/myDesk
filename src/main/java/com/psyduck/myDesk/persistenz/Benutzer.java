package com.psyduck.myDesk.persistenz;

public class Benutzer {

    private final String email;
    private final String passwort;
    private final String name;

    public Benutzer(String email, String passwort, String name) {
        this.email = email;
        this.passwort = passwort;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswort() {
        return passwort;
    }

    public String getName() {
        return name;
    }

}