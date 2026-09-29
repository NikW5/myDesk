package com.psyduck.myDesk.persistenz;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NachrichtRepository extends JpaRepository<Nachricht, Integer> {

    List<Nachricht> findByEmpfaenger(Benutzer empfaenger);

    long countByEmpfaenger(Benutzer empfaenger);

    long countByEmpfaengerAndGelesenFalse(Benutzer empfaenger);

    Optional<Nachricht> findByIdAndEmpfaenger(
            Integer id,
            Benutzer empfaenger);

}

