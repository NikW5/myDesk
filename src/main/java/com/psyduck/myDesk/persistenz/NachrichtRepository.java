package com.psyduck.myDesk.persistenz;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NachrichtRepository extends JpaRepository<Nachricht, Integer> {

    List<Nachricht> findByEmpfaenger(Benutzer empfaenger);
    List<Nachricht> findByAbsender(Benutzer absender);

    long countByEmpfaenger(Benutzer empfaenger);
    long countByEmpfaengerAndGelesenFalse(Benutzer empfaenger);

    Optional<Nachricht> findByIdAndEmpfaenger(
            Integer id,
            Benutzer empfaenger
    ); 
    
    @Modifying
    @Query("UPDATE Nachricht n SET n.absender = NULL WHERE n.absender.id = :id")
    void setAbsenderNull(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Nachricht n SET n.empfaenger = NULL WHERE n.empfaenger.id = :id")
    void setEmpfaengerNull(@Param("id") Integer id);
}