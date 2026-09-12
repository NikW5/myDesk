package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerSession;
import com.psyduck.myDesk.persistenz.NachrichtService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(
    value = "dashboard",
    layout = MainLayout.class
)
public class DashboardView extends VerticalLayout {

    private final NachrichtService nachrichtService;

    public DashboardView(NachrichtService nachrichtService) {

        this.nachrichtService = nachrichtService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        Benutzer benutzer =
            BenutzerSession.getAktuellerBenutzer();

        String name = benutzer != null
            ? benutzer.getName()
            : "";

        H2 begruessung = new H2(
            name.isEmpty()
                ? "Willkommen bei myDesk"
                : "Willkommen zurück, " + name + "!"
        );

        Span untertitel = new Span(
            "Was möchtest du heute erledigen?"
        );

        VerticalLayout titel = new VerticalLayout(
            begruessung,
            untertitel
        );

        titel.setPadding(false);
        titel.setSpacing(false);

        long anzahlUngeleseneNachrichten =
            benutzer != null
                ? nachrichtService
                    .getAnzahlUngeleseneNachrichten(benutzer)
                : 0;

        String postfachBeschreibung;

        if (anzahlUngeleseneNachrichten == 0) {

            postfachBeschreibung =
                "Keine neuen Nachrichten";

        } else if (anzahlUngeleseneNachrichten == 1) {

            postfachBeschreibung =
                "1 neue Nachricht";

        } else {

            postfachBeschreibung =
                anzahlUngeleseneNachrichten
                    + " neue Nachrichten";
        }

        HorizontalLayout karten =
            new HorizontalLayout();

        karten.setWidthFull();

        karten.setJustifyContentMode(
            FlexComponent.JustifyContentMode.CENTER
        );

        karten.setAlignItems(
            FlexComponent.Alignment.START
        );

        karten.add(

            erstelleKarte(
                VaadinIcon.ENVELOPE,
                "Postfach",
                postfachBeschreibung,
                () -> UI.getCurrent()
                    .navigate(PostfachView.class)
            ),

            erstelleKarte(
                VaadinIcon.COMMENTS,
                "Chat",
                "Nachrichten und Gespräche",
                () -> UI.getCurrent()
                    .navigate(ChatView.class)
            ),

            erstelleKarte(
                VaadinIcon.CALENDAR,
                "Kalender",
                "Termine und Aufgaben",
                () -> UI.getCurrent()
                    .navigate(KalenderView.class)
            ),

            erstelleKarte(
                VaadinIcon.CHECK,
                "To-Dos",
                "Deine Aufgaben",
                () -> UI.getCurrent()
                    .navigate(ToDoView.class)
            )
        );

        add(
            titel,
            karten
        );

        expand(karten);
    }

    private Card erstelleKarte(
        VaadinIcon icon,
        String titel,
        String beschreibung,
        Runnable aktion
    ) {

        Span iconSpan =
            new Span(icon.create());

        iconSpan.getStyle()
            .set(
                "font-size",
                "48px"
            )
            .set(
                "color",
                "var(--lumo-primary-color)"
            );

        Span titelSpan =
            new Span(titel);

        titelSpan.getStyle()
            .set(
                "font-size",
                "var(--lumo-font-size-l)"
            )
            .set(
                "font-weight",
                "600"
            );

        Span beschreibungSpan =
            new Span(beschreibung);

        beschreibungSpan.getStyle()
            .set(
                "color",
                "var(--lumo-secondary-text-color)"
            );

        VerticalLayout inhalt =
            new VerticalLayout(
                iconSpan,
                titelSpan,
                beschreibungSpan
            );

        inhalt.setPadding(true);
        inhalt.setSpacing(false);

        inhalt.setAlignItems(
            FlexComponent.Alignment.CENTER
        );

        inhalt.setJustifyContentMode(
            FlexComponent.JustifyContentMode.CENTER
        );

        Card karte =
            new Card();

        karte.add(
            inhalt
        );

        karte.setWidth("220px");
        karte.setHeight("180px");

        karte.getStyle()
            .set(
                "cursor",
                "pointer"
            )
            .set(
                "transition",
                "transform 0.15s, box-shadow 0.15s"
            );

        karte.getElement().addEventListener(
        	    "click",
        	    event -> aktion.run()
        	);


        return karte;
    }
}
