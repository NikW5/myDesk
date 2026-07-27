package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Kopfzeile extends HorizontalLayout {

    public enum Typ {
        LOGIN,
        DASHBOARD,
        POSTFACH,
        NACHRICHT_SENDEN
    }

    public Kopfzeile(Typ typ) {
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.END);

        switch (typ) {

            case LOGIN -> {
                Button abbrechen = new Button("Abbrechen");
                Button abmelden = new Button("Abmelden");

                add(abbrechen, abmelden);
            }

            case DASHBOARD -> {
                Button abmelden = new Button("Abmelden");

                HorizontalLayout buttons = new HorizontalLayout(abmelden);
                buttons.setSpacing(true);

                add(buttons);
            }

            case POSTFACH -> {
                H2 titel = new H2("Postfach");

                Button aktualisieren = new Button("Aktualisieren");
                Button neueNachricht = new Button("Neue Nachricht");
                Button abmelden = new Button("Abmelden");

                HorizontalLayout buttons = new HorizontalLayout(
                        aktualisieren,
                        neueNachricht,
                        abmelden
                );

                buttons.setSpacing(true);

                add(titel, buttons);
                expand(titel);
            }

            case NACHRICHT_SENDEN -> {
                Button abbrechen = new Button("Abbrechen");
                Button abmelden = new Button("Abmelden");

                HorizontalLayout buttons = new HorizontalLayout(
                        abbrechen,
                        abmelden
                );

                buttons.setSpacing(true);

                add(buttons);
            }
        }
    }
}

