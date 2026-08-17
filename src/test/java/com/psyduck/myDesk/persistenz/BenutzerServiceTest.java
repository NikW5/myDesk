package com.psyduck.myDesk.persistenz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BenutzerServiceTest {

	// namen angepasst
    @Test
    void anmelden_gibtTrueBeiBestehendemBenutzerIAahZurueck() { 
        boolean ergebnis = BenutzerService.anmelden(
                "i-aah@test.de",
                "1234"
        );

        assertTrue(ergebnis);
    }

    @Test
    void anmelden_gibtTrueBeiBestehendemBenutzerEntonZurueck() {
        boolean ergebnis = BenutzerService.anmelden(
                "enton@test.de",
                "abcd"
        );

        assertTrue(ergebnis);
    }

    @Test
    void anmelden_gibtFalseBeiUnbekanntemBenutzerZurueck() {
        boolean ergebnis = BenutzerService.anmelden(
                "unbekannt@test.de",
                "1234"
        );

        assertFalse(ergebnis);
    }

    @Test
    void anmelden_gibtFalseBeiFalschemPasswortZurueck() {
        boolean ergebnis = BenutzerService.anmelden(
                "i-aah@test.de",
                "falschesPasswort"
        );

        assertFalse(ergebnis);
    }

    @Test
    void anmelden_gibtFalseBeiLeeremBenutzernamenZurueck() {
        boolean ergebnis = BenutzerService.anmelden(
                "",
                "1234"
        );

        assertFalse(ergebnis);
    }
}
