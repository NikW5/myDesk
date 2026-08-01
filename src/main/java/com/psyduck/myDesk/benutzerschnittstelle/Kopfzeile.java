package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Kopfzeile extends Header {

    public enum Typ {
        LOGIN,
        DASHBOARD,
        POSTFACH,
        NACHRICHT_SENDEN
    }

    public Kopfzeile(Typ typ) {
    	setWidthFull();
    	
    	HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);

        switch (typ) {

            case LOGIN -> {
            	Button registrieren = new Button("Registrieren");
                layout.add(registrieren);
            }

            case DASHBOARD -> {
            	
            	H1 titel = new H1("Dashboard");
            	
                Button abmelden = new Button("Abmelden");

                HorizontalLayout buttons = new HorizontalLayout(abmelden);
                buttons.setSpacing(true);

                layout.add(titel, buttons);
                layout.expand(titel);
            }

            case POSTFACH -> {
                H1 titel = new H1("Postfach");

                Button aktualisieren = new Button("Aktualisieren");
                Button neueNachricht = new Button("Neue Nachricht");
                Button abmelden = new Button("Abmelden");

                HorizontalLayout buttons = new HorizontalLayout(
                        aktualisieren,
                        neueNachricht,
                        abmelden
                );

                buttons.setSpacing(true);

                layout.add(titel, buttons);
                layout.expand(titel);
            }

            case NACHRICHT_SENDEN -> {
            	
            	H1 titel = new H1("Neue Nachricht");
            	
                Button abbrechen = new Button("Abbrechen");
                Button abmelden = new Button("Abmelden");

                HorizontalLayout buttons = new HorizontalLayout(
                        abbrechen,
                        abmelden
                );

                buttons.setSpacing(true);

                layout.add(titel, buttons);
                layout.expand(titel);
            }
        }
        
        add(layout);
    }
}

