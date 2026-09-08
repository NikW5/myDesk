package com.psyduck.myDesk.benutzerschnittstelle.layout;

import com.psyduck.myDesk.benutzerschnittstelle.DashboardView;
import com.psyduck.myDesk.benutzerschnittstelle.LoginView;
import com.psyduck.myDesk.benutzerschnittstelle.NachrichtSendenView;
import com.psyduck.myDesk.benutzerschnittstelle.PostfachView;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerSession;
import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Kopfzeile extends Header {

    public Kopfzeile() {

        setWidthFull();

        getStyle()
                .set("top", "0")
                .set("z-index", "1000")
                .set("background", "white")
                .set("padding", "12px 24px")
                .set("box-sizing", "border-box")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
    }

    public void setTyp(Kopfzeilentyp typ) {

        removeAll();

        switch (typ) {

            case LOGIN -> {

                H1 titel = new H1(":)");

                HorizontalLayout layout = new HorizontalLayout(titel);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);

                add(layout);
            }

            case DASHBOARD -> {

                Benutzer benutzer = BenutzerSession.getAktuellerBenutzer();

                String name = "";

                if (benutzer != null) {
                    name = benutzer.getName();
                }

                H1 titel = new H1("Dashboard");

                Span benutzername = new Span("Hallo " + name);

                Button abmelden = erstelleAbmeldenButton();

                HorizontalLayout buttons = new HorizontalLayout(abmelden);
                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, benutzername, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case POSTFACH -> {

                H1 titel = new H1("Postfach");

                Button dashboard = erstelleDashboardButton();      
                Button aktualisieren = new Button("Aktualisieren"); // Button macht noch nichts
                Button neueNachricht = erstelleNeueNachrichtButton();
                Button abmelden = erstelleAbmeldenButton();

                HorizontalLayout buttons = new HorizontalLayout(dashboard, aktualisieren, neueNachricht, abmelden);
                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case NACHRICHT_SENDEN -> {

                H1 titel = new H1("Neue Nachricht");

                Button abbrechen = erstelleAbbrechenButton();
                Button abmelden = erstelleAbmeldenButton();

                HorizontalLayout buttons = new HorizontalLayout(abbrechen, abmelden);
                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case CHAT -> {

                H1 titel = new H1("Chat");

                Button dashboard = erstelleDashboardButton();
                Button abmelden = erstelleAbmeldenButton();

                HorizontalLayout buttons = new HorizontalLayout(dashboard, abmelden);
                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case KALENDER -> {

                H1 titel = new H1("Kalender");

                Button dashboard = erstelleDashboardButton();
                Button abmelden = erstelleAbmeldenButton();

                HorizontalLayout buttons = new HorizontalLayout(dashboard, abmelden);
                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case TODO -> {

                H1 titel = new H1("To-Do");

                Button dashboard = erstelleDashboardButton();
                Button abmelden = erstelleAbmeldenButton();

                HorizontalLayout buttons = new HorizontalLayout(dashboard, abmelden);
                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }
        }
    }
    
    private Button erstelleAbmeldenButton() {
        return new Button("Abmelden", event -> {
        			BenutzerSession.abmelden(); 
        			UI.getCurrent().navigate(LoginView.class);
                }
        );
    }
    
    private Button erstelleDashboardButton() {
    	return new Button("Dashboard", event -> 
    			UI.getCurrent().navigate(DashboardView.class)
    	);
    }
    
    private Button erstelleAbbrechenButton() {
    	return new Button("Abbrechen", event -> 
    		UI.getCurrent().navigate(PostfachView.class)
        );
    }

    private Button erstelleNeueNachrichtButton() {
    	return new Button("Neue Nachricht", event -> 
    		UI.getCurrent().navigate(NachrichtSendenView.class)
        );
    }
}
