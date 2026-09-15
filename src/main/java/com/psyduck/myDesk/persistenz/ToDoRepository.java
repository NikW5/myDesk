package com.psyduck.myDesk.persistenz;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {

    List<ToDo> findByBenutzer(Benutzer benutzer);
}
