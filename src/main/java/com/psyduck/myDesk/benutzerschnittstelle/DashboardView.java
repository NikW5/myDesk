package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerSession;
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

@StyleSheet("styles.css")
@Route(
    value = "dashboard",
    layout = MainLayout.class
)
public class DashboardView extends VerticalLayout {

    public DashboardView() {
    	
    	// Hintergrund
    	setSizeFull();
        addClassName("dashboard-background");
        
        // Kopfzeile
        Benutzer benutzer = BenutzerSession.getAktuellerBenutzer();
        String name = benutzer != null ? benutzer.getName() : "";
    	
        H2 begruessung = new H2(name.isEmpty() ? "Willkommen bei myDesk" : "Willkommen zurück, " + name + "!");
        begruessung.addClassName("dashboard-title");
    	
        Span untertitel = new Span("Was möchtest du heute erledigen?");
        untertitel.addClassName("dashboard-subtitle");
    	
        VerticalLayout textLayout = new VerticalLayout(begruessung, untertitel);
        textLayout.setPadding(true);
        textLayout.setSpacing(false);
        textLayout.setAlignItems(Alignment.START);
        
        add(textLayout);

        // Karten
        Card kartePostfach = erstelleKarte(
                "card-postfach",
                "mail",
                "Postfach",
                "3",
                "neue Nachrichten",
                () -> UI.getCurrent()
                .navigate(PostfachView.class)
        );

        Card karteChat = erstelleKarte(
                "card-chat",
                "chat",
                "Chat",
                "2",
                "ungelesene Nachrichten",
                () -> UI.getCurrent()
                .navigate(ChatView.class)
        );

        Card karteKalender = erstelleKarte(
                "card-kalender",
                "calendar_month",
                "Kalender",
                "5",
                "heutige Einträge",
                () -> UI.getCurrent()
                .navigate(KalenderView.class)
        );

        Card karteTodos = erstelleKarte(
                "card-todos",
                "check_box",
                "To-Dos",
                "3",
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
    
    private Card erstelleKarte(
            String farbKlasse,
            String iconName,
            String titelText,
            String subtitleText,
            String contentText,
            Runnable aktion
    ) {
        Card karte = new Card();
        karte.addThemeVariants(CardVariant.HORIZONTAL);
        karte.addClassNames("dashboard-card", farbKlasse);

        Span icon = new Span(iconName);
        icon.getElement().getClassList().add("material-symbols-rounded");
        icon.addClassNames("card-icon-circle", "card-icon-circle-" + iconName);
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
