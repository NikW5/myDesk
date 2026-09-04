package com.psyduck.myDesk.persistenz;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NachrichtRepository extends JpaRepository<Nachricht, Integer> {

    List<Nachricht> findByEmpfaenger(Benutzer empfaenger);
    
    long countByEmpfaenger(Benutzer empfaenger);

}
