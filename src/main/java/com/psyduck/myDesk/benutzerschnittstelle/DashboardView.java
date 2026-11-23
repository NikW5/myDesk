
package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.NachrichtService;
import com.psyduck.myDesk.persistenz.ToDoRepository;
import com.psyduck.myDesk.security.AktuellerBenutzerService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import org.springframework.context.MessageSource;

import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@StyleSheet("styles.css")
@Route(
        value = "dashboard",
        layout = MainLayout.class
)
@PermitAll
public class DashboardView extends VerticalLayout {

    private final AktuellerBenutzerService aktuellerBenutzerService;
    private final NachrichtService nachrichtService;
    private final ToDoRepository toDoRepository;
    private final MessageSource messageSource;

    private static final List<String> UNTERTEXTE = List.of(
            "dashboard.subtitle.1",
            "dashboard.subtitle.2",
            "dashboard.subtitle.3",
            "dashboard.subtitle.4",
            "dashboard.subtitle.5",
            "dashboard.subtitle.6"
    );

    private static final Random RANDOM = new Random();

    public DashboardView(
            AktuellerBenutzerService aktuellerBenutzerService,
            NachrichtService nachrichtService,
            ToDoRepository toDoRepository,
            MessageSource messageSource) {

        this.aktuellerBenutzerService = aktuellerBenutzerService;
        this.nachrichtService = nachrichtService;
        this.toDoRepository = toDoRepository;
        this.messageSource = messageSource;

        setSizeFull();
        addClassName("dashboard-background");

        Benutzer benutzer =
                aktuellerBenutzerService.getAktuellerBenutzer();

        String name = benutzer != null
                ? benutzer.getName()
                : "";

        H2 begruessung = new H2(
                erstelleBegruessung(name)
        );

        begruessung.addClassName("dashboard-title");

        Span untertitel = new Span(
                waehleZufaelligenUntertext()
        );

        untertitel.addClassName("dashboard-subtitle");

        VerticalLayout textLayout = new VerticalLayout(
                begruessung,
                untertitel
        );

        textLayout.setPadding(true);
        textLayout.setSpacing(false);
        textLayout.setAlignItems(Alignment.START);

        add(textLayout);

        long anzahlPostfach = 0;
        long anzahlKalender = 0;
        long anzahlTodos = 0;

        if (benutzer != null) {

            anzahlPostfach =
                    nachrichtService.getAnzahlUngeleseneNachrichten(
                            benutzer
                    );

            anzahlKalender =
                    toDoRepository.countByBenutzerAndFaelligAm(
                            benutzer,
                            LocalDate.now()
                    );

            anzahlTodos =
                    toDoRepository.countByBenutzerAndErledigtFalse(
                            benutzer
                    );
        }

        Card kartePostfach = erstelleKarte(
                "card-postfach",
                "mail",
                getText("dashboard.card.postfach"),
                getText("dashboard.card.postfach.subtitle"),
                String.valueOf(anzahlPostfach),
                () -> UI.getCurrent().navigate(PostfachView.class)
        );

        Card karteKalender = erstelleKarte(
                "card-kalender",
                "calendar_month",
                getText("dashboard.card.kalender"),
                getText("dashboard.card.kalender.subtitle"),
                String.valueOf(anzahlKalender),
                () -> UI.getCurrent().navigate(KalenderView.class)
        );

        Card karteTodos = erstelleKarte(
                "card-todos",
                "check_box",
                getText("dashboard.card.todos"),
                getText("dashboard.card.todos.subtitle"),
                String.valueOf(anzahlTodos),
                () -> UI.getCurrent().navigate(ToDoView.class)
        );

        HorizontalLayout karten = new HorizontalLayout(
                kartePostfach,
                karteKalender,
                karteTodos
        );

        karten.setSpacing(true);
        karten.setPadding(true);
        karten.setWidthFull();

        karten.setJustifyContentMode(
                JustifyContentMode.CENTER
        );

        karten.setAlignItems(Alignment.START);

        add(karten);
    }

    private String erstelleBegruessung(String name) {

        LocalTime jetzt = LocalTime.now();

        String schluessel;

        if (jetzt.isBefore(LocalTime.NOON)) {

            schluessel = "dashboard.greeting.morning";

        } else if (jetzt.isBefore(LocalTime.of(18, 0))) {

            schluessel = "dashboard.greeting.day";

        } else {

            schluessel = "dashboard.greeting.evening";
        }

        String begruessung = getText(schluessel);

        if (name.isBlank()) {
            return begruessung;
        }

        return messageSource.getMessage(
                "dashboard.greeting.named",
                new Object[]{begruessung, name},
                begruessung + ", " + name + "!",
                Locale.GERMAN
        );
    }

    private String waehleZufaelligenUntertext() {

        String schluessel = UNTERTEXTE.get(
                RANDOM.nextInt(UNTERTEXTE.size())
        );

        return getText(schluessel);
    }

    private String getText(String schluessel) {

        return messageSource.getMessage(
                schluessel,
                null,
                schluessel,
                Locale.GERMAN
        );
    }

    private Card erstelleKarte(
            String farbKlasse,
            String iconName,
            String titelText,
            String subtitleText,
            String contentText,
            Runnable aktion) {

        Card karte = new Card();

        karte.addThemeVariants(CardVariant.HORIZONTAL);

        karte.addClassNames(
                "dashboard-card",
                farbKlasse
        );

        Span icon = new Span(iconName);

        icon.getElement()
                .getClassList()
                .add("material-symbols-rounded");

        icon.addClassNames(
                "card-icon-circle",
                "card-icon-circle-" + iconName
        );

        karte.setMedia(icon);

        Div title = new Div(titelText);
        karte.setTitle(title);

        Div subtitle = new Div(subtitleText);
        karte.setSubtitle(subtitle);

        Div content = new Div(contentText);
        karte.add(content);

        karte.getElement().addEventListener(
                "click",
                event -> aktion.run()
        );

        return karte;
    }
}
