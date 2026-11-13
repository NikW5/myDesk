
package com.psyduck.myDesk.persistenz;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KalendereintragRepository extends JpaRepository<Kalendereintrag, Integer> {

    List<Kalendereintrag> findByBenutzer(Benutzer benutzer);

    Optional<Kalendereintrag> findByIdAndBenutzer(Integer id, Benutzer benutzer);
}
