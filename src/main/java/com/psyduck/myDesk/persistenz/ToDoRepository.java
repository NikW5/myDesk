package com.psyduck.myDesk.persistenz;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {

    List<ToDo> findByBenutzer(Benutzer benutzer);

    long countByBenutzerAndErledigtFalse(Benutzer benutzer);

    long countByBenutzerAndFaelligAm(
            Benutzer benutzer,
            LocalDate faelligAm
    );
}

