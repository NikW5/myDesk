package com.psyduck.myDesk.persistenz;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BenutzerRepository extends JpaRepository<Benutzer, Integer> {

Optional<Benutzer> findByEmailIgnoreCase(String email);

Optional<Benutzer> findByNameIgnoreCase(String name);


}