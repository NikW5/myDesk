package com.psyduck.myDesk.persistenz;

import jakarta.persistence.*;

@Entity
@Table(name = "benutzer")
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwort;

    @Column(name = "benutzername", nullable = false, unique = true)
    private String name;

    private String vorname;
    private String nachname;

    protected Benutzer() {}

    public Benutzer(String email, String passwort, String name) {
        this.email = email;
        this.passwort = passwort;
        this.name = name;
    }

    public Integer getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswort() { return passwort; }
    public String getName() { return name; }
    public String getVorname() { return vorname; }
    public String getNachname() { return nachname; }

    public void setEmail(String email) { this.email = email; }
    public void setPasswort(String passwort) { this.passwort = passwort; }
    public void setName(String name) { this.name = name; }
    public void setVorname(String vorname) { this.vorname = vorname; }
    public void setNachname(String nachname) { this.nachname = nachname; }
}