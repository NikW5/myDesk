package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Kopfzeile extends Header {

    public Kopfzeile(Kopfzeilentyp typ) {
        setWidthFull();
        
        Button abmelden = new Button("Abmelden");
        abmelden.addClickListener(event -> UI.getCurrent().navigate(LoginView.class));
        Button dashboard = new Button("Dashboard");
        dashboard.addClickListener(event -> UI.getCurrent().navigate(DashboardView.class));

        switch (typ) {

            case LOGIN -> {
            	Button registrieren = new Button("Registrieren");
            	registrieren.addClickListener(event -> UI.getCurrent().navigate(RegistrierenView.class));

                add(registrieren);
            }

            case DASHBOARD -> {
            	
            	H1 titel = new H1("Dashboard");

                HorizontalLayout buttons = new HorizontalLayout(abmelden);
                buttons.setSpacing(true);

                add(titel, buttons);
            }

            case POSTFACH -> {
                H1 titel = new H1("Postfach");

                Button aktualisieren = new Button("Aktualisieren");
                Button neueNachricht = new Button("Neue Nachricht");
                neueNachricht.addClickListener(event -> UI.getCurrent().navigate(NachrichtSendenView.class));

                HorizontalLayout buttons = new HorizontalLayout(
                		dashboard,
                		aktualisieren,
                        neueNachricht,
                        abmelden
                );

                buttons.setSpacing(true);

                add(titel, buttons);
            }

            case NACHRICHT_SENDEN -> {
            	
            	H1 titel = new H1("Neue Nachricht");
            	
                Button abbrechen = new Button("Abbrechen");
                abbrechen.addClickListener(event -> UI.getCurrent().navigate(PostfachView.class));

                HorizontalLayout buttons = new HorizontalLayout(
                        abbrechen,
                        abmelden
                );

                buttons.setSpacing(true);

                add(titel, buttons);
            }
            
            case CHAT -> {
            	
            	H1 titel = new H1("Chat");

                HorizontalLayout buttons = new HorizontalLayout(dashboard, abmelden);
                buttons.setSpacing(true);

                add(titel, buttons);
            }
            
            case KALENDER -> {
            	
            	H1 titel = new H1("Kalender");

                HorizontalLayout buttons = new HorizontalLayout(dashboard, abmelden);
                buttons.setSpacing(true);

                add(titel, buttons);
            }

			case TODO -> {
				
				H1 titel = new H1("To-Do");
			
			    HorizontalLayout buttons = new HorizontalLayout(dashboard, abmelden);
			    buttons.setSpacing(true);
			
			    add(titel, buttons);
			}
        }
        
    }
}

