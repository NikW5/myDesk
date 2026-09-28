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

import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

	private static final List<String> UNTERTEXTE = List.of(
	    "Was möchtest du heute erledigen?",
	    "Womit möchtest du heute starten?",
	    "Was steht heute auf deiner Liste?",
	    "Bereit für deine nächsten Aufgaben?",
	    "Was möchtest du heute erreichen?",
	    "Welche Aufgabe möchtest du als Nächstes angehen?"
	);

	private static final Random RANDOM = new Random();

	public DashboardView(
	        AktuellerBenutzerService aktuellerBenutzerService,
	        NachrichtService nachrichtService,
	        ToDoRepository toDoRepository) {

	    this.aktuellerBenutzerService = aktuellerBenutzerService;
	    this.nachrichtService = nachrichtService;
	    this.toDoRepository = toDoRepository;

	    setSizeFull();
	    addClassName("dashboard-background");

	    Benutzer benutzer =
	            aktuellerBenutzerService.getAktuellerBenutzer();

	    String name =
	            benutzer != null
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

	        anzahlPostfach = nachrichtService.getAnzahlUngeleseneNachrichten(benutzer);
	        anzahlKalender = toDoRepository.countByBenutzerAndFaelligAm(benutzer, LocalDate.now());
	        anzahlTodos = toDoRepository.countByBenutzerAndErledigtFalse(benutzer);
	    }

	    long anzahlChat = 0;

	    Card kartePostfach = erstelleKarte(
	            "card-postfach",
	            "mail",
	            "Postfach",
	            String.valueOf(anzahlPostfach),
	            "neue Nachrichten",
	            () -> UI.getCurrent()
	                    .navigate(PostfachView.class)
	    );

	    Card karteChat = erstelleKarte(
	            "card-chat",
	            "chat",
	            "Chat",
	            String.valueOf(anzahlChat),
	            "ungelesene Nachrichten",
	            () -> UI.getCurrent()
	                    .navigate(ChatView.class)
	    );

	    Card karteKalender = erstelleKarte(
	            "card-kalender",
	            "calendar_month",
	            "Kalender",
	            String.valueOf(anzahlKalender),
	            "heutige Einträge",
	            () -> UI.getCurrent()
	                    .navigate(KalenderView.class)
	    );

	    Card karteTodos = erstelleKarte(
	            "card-todos",
	            "check_box",
	            "To-Dos",
	            String.valueOf(anzahlTodos),
	            "offene Aufgaben",
	            () -> UI.getCurrent()
	                    .navigate(ToDoView.class)
	    );

	    HorizontalLayout karten = new HorizontalLayout(
	            kartePostfach,
	            karteChat,
	            karteKalender,
	            karteTodos
	    );

	    karten.setSpacing(true);
	    karten.setPadding(true);
	    karten.setWidthFull();
	    karten.setJustifyContentMode(JustifyContentMode.CENTER);
	    karten.setAlignItems(Alignment.START);

	    add(karten);
	}

	private String erstelleBegruessung(String name) {

	    LocalTime jetzt = LocalTime.now();
	    String begruessung;

	    if (jetzt.isBefore(LocalTime.NOON)) {
	        begruessung = "Guten Morgen";
	    } else if (jetzt.isBefore(LocalTime.of(18, 0))) {
	        begruessung = "Hallo";
	    } else {
	        begruessung = "Guten Abend";
	    }

	    if (name.isEmpty()) {
	        return begruessung;
	    }

	    return begruessung + ", " + name + "!";
	}

	private String waehleZufaelligenUntertext() {

	    return UNTERTEXTE.get(RANDOM.nextInt(UNTERTEXTE.size()));
	}

	private Card erstelleKarte(
	        String farbKlasse,
	        String iconName,
	        String titelText,
	        String subtitleText,
	        String contentText,
	        Runnable aktion
	) {

	    Card karte = new Card();

	    karte.addThemeVariants(
	            CardVariant.HORIZONTAL
	    );

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