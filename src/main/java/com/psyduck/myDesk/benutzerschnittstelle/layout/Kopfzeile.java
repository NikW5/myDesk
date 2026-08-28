package com.psyduck.myDesk.benutzerschnittstelle.layout;

import com.psyduck.myDesk.benutzerschnittstelle.DashboardView;
import com.psyduck.myDesk.benutzerschnittstelle.LoginView;
import com.psyduck.myDesk.benutzerschnittstelle.NachrichtSendenView;
import com.psyduck.myDesk.benutzerschnittstelle.PostfachView;
import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

public class Kopfzeile extends Header {

    public Kopfzeile(Kopfzeilentyp typ) {
        setWidthFull();

        getStyle()
                .set("top", "0")
                .set("z-index", "1000")
                .set("background", "white")
                .set("padding", "12px 24px")
                .set("box-sizing", "border-box")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        Button abmelden = new Button("Abmelden");
        abmelden.addClickListener(
                event -> UI.getCurrent().navigate(LoginView.class)
        );

        Button dashboard = new Button("Dashboard");
        dashboard.addClickListener(
                event -> UI.getCurrent().navigate(DashboardView.class)
        );

        switch (typ) {

            case LOGIN -> {
            	H1 titel = new H1(":)");
            	
            	HorizontalLayout layout = new HorizontalLayout(titel);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(titel);
            }

            case DASHBOARD -> {
                H1 titel = new H1("Dashboard");

                HorizontalLayout buttons = new HorizontalLayout(abmelden);
                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case POSTFACH -> {
                H1 titel = new H1("Postfach");

                Button aktualisieren = new Button("Aktualisieren");

                Button neueNachricht = new Button("Neue Nachricht");
                neueNachricht.addClickListener(
                        event -> UI.getCurrent().navigate(NachrichtSendenView.class)
                );

                HorizontalLayout buttons = new HorizontalLayout(
                        dashboard,
                        aktualisieren,
                        neueNachricht,
                        abmelden
                );

                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case NACHRICHT_SENDEN -> {
                H1 titel = new H1("Neue Nachricht");

                Button abbrechen = new Button("Abbrechen");
                abbrechen.addClickListener(
                        event -> UI.getCurrent().navigate(PostfachView.class)
                );

                HorizontalLayout buttons = new HorizontalLayout(
                        abbrechen,
                        abmelden
                );

                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case CHAT -> {
                H1 titel = new H1("Chat");

                HorizontalLayout buttons = new HorizontalLayout(
                        dashboard,
                        abmelden
                );

                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case KALENDER -> {
                H1 titel = new H1("Kalender");

                HorizontalLayout buttons = new HorizontalLayout(
                        dashboard,
                        abmelden
                );

                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }

            case TODO -> {
                H1 titel = new H1("To-Do");

                HorizontalLayout buttons = new HorizontalLayout(
                        dashboard,
                        abmelden
                );

                buttons.setSpacing(true);

                HorizontalLayout layout = new HorizontalLayout(titel, buttons);
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                layout.expand(titel);

                add(layout);
            }    
        }
    }
}