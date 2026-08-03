package com.psyduck.myDesk.persistenz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NachrichtService {

	private static final List<Nachricht> nachrichten = new ArrayList<>();
	
	static {
		
		Nachricht dummy1 = new Nachricht();
        dummy1.setBenutzer("Max Mustermann");
        dummy1.setTitel("Willkommen bei myDesk");
        dummy1.setVorschau("Herzlich willkommen bei myDesk...");
        dummy1.setInhalt("""
                Hallo,

                herzlich willkommen bei myDesk!

                Wir freuen uns, dass Sie unser System nutzen.
                Bei Fragen steht Ihnen der Support jederzeit zur Verfügung.

                Viele Grüße
                Ihr myDesk-Team
                """);
        dummy1.setEmpfangenAm(LocalDateTime.now().minusHours(2));

        Nachricht dummy2 = new Nachricht();
        dummy2.setBenutzer("IT-Support");
        dummy2.setTitel("Passwort geändert");
        dummy2.setVorschau("Ihr Passwort wurde erfolgreich geändert...");
        dummy2.setInhalt("""
                Guten Tag,

                Ihr Passwort wurde erfolgreich geändert.

                Sollten Sie diese Änderung nicht selbst vorgenommen haben,
                wenden Sie sich bitte umgehend an den IT-Support.
                """);
        dummy2.setEmpfangenAm(LocalDateTime.now().minusDays(1));

        Nachricht dummy3 = new Nachricht();
        dummy3.setBenutzer("Personalabteilung");
        dummy3.setTitel("Urlaubsantrag genehmigt");
        dummy3.setVorschau("Ihr Urlaubsantrag wurde genehmigt...");
        dummy3.setInhalt("""
                Hallo,

                Ihr Urlaubsantrag wurde genehmigt.

                Wir wünschen Ihnen einen erholsamen Urlaub!

                Mit freundlichen Grüßen
                Personalabteilung
                """);
        dummy3.setEmpfangenAm(LocalDateTime.now().minusDays(5));
            
        nachrichten.add(dummy1);
        nachrichten.add(dummy2);
        nachrichten.add(dummy3);
	}
	
	public static List<Nachricht> getNachrichten() {
        return nachrichten;
    }

    public static int getAnzahlNachrichten() {
        return nachrichten.size();
    }
}
