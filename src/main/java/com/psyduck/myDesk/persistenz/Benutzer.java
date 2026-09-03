package com.psyduck.myDesk.persistenz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

protected Benutzer() {
   
}

public Benutzer(String email, String passwort, String name) {
    this.email = email;
    this.passwort = passwort;
    this.name = name;
}

public Integer getId() {
    return id;
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

public void setEmail(String email) {
    this.email = email;
}

public void setPasswort(String passwort) {
    this.passwort = passwort;
}

public void setName(String name) {
    this.name = name;
}


}