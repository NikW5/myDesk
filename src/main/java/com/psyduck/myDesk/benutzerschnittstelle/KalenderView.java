package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendar.Option;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.ToDo;
import com.psyduck.myDesk.persistenz.ToDoRepository;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@StyleSheet("styles.css")
@Route(value = "kalender", layout = MainLayout.class)
@PermitAll
public class KalenderView extends VerticalLayout {

    private final ToDoRepository toDoRepository;
    private final AktuellerBenutzerService aktuellerBenutzerService;

    public KalenderView(ToDoRepository toDoRepository,
                        AktuellerBenutzerService aktuellerBenutzerService) {
        this.toDoRepository = toDoRepository;
        this.aktuellerBenutzerService = aktuellerBenutzerService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("kalender-background");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.addClassName("kalender-content");

        H2 titel = new H2("Kalender");
        titel.addClassName("kalender-title");

        Span untertitel = new Span("Hier findest du deine geplanten Aufgaben.");
        untertitel.addClassName("kalender-subtitle");

        VerticalLayout kopfbereich = new VerticalLayout(titel, untertitel);
        kopfbereich.setPadding(false);
        kopfbereich.setSpacing(false);

        VerticalLayout kalenderPanel = erstelleKalenderPanel();

        content.add(kopfbereich, kalenderPanel);
        content.expand(kalenderPanel);
        add(content);
    }

    private VerticalLayout erstelleKalenderPanel() {
        FullCalendar kalender = erstelleKalender();
        HorizontalLayout header = erstelleKalenderheader(kalender);

        VerticalLayout panel = new VerticalLayout(header, kalender);
        panel.setSizeFull();
        panel.setPadding(false);
        panel.setSpacing(false);
        panel.expand(kalender);
        panel.addClassName("kalender-panel");

        return panel;
    }

    private FullCalendar erstelleKalender() {
        FullCalendar kalender = FullCalendarBuilder.create().build();
        kalender.setSizeFull();

        kalender.setOption(Option.LOCALE, Locale.GERMAN);
        kalender.setOption(Option.HEADER_TOOLBAR, false);
        kalender.setOption(Option.FIRST_DAY, 1);

        fuegeTodosZumKalenderHinzu(kalender);

        return kalender;
    }

    private void fuegeTodosZumKalenderHinzu(FullCalendar kalender) {
        Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();
        if (benutzer == null) return;

        for (ToDo aufgabe : toDoRepository.findByBenutzer(benutzer)) {
            if (aufgabe.getFaelligAm() == null) continue;

            Entry eintrag = new Entry(aufgabe.getId().toString());
            eintrag.setTitle(aufgabe.isErledigt()
                    ? "✓ " + aufgabe.getText()
                    : aufgabe.getText());
            eintrag.setStart(aufgabe.getFaelligAm());
            eintrag.setAllDay(true);

            if (aufgabe.isErledigt()) {
                eintrag.setColor("#4e8f53");
                eintrag.setTextColor("#ffffff");
            } else {
                eintrag.setColor("#f4700a");
                eintrag.setTextColor("#ffffff");
            }

            kalender.getEntryProvider().asInMemory().addEntry(eintrag);
        }
    }

    private HorizontalLayout erstelleKalenderheader(FullCalendar kalender) {
        Button previous = new Button("‹", event -> kalender.previous());
        previous.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        previous.addClassName("kalender-nav-button");

        Button today = new Button("Heute", event -> kalender.today());
        today.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        today.addClassName("kalender-today-button");

        Button next = new Button("›", event -> kalender.next());
        next.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        next.addClassName("kalender-nav-button");

        HorizontalLayout navigation = new HorizontalLayout(previous, today, next);
        navigation.setPadding(false);
        navigation.setSpacing(false);
        navigation.setAlignItems(FlexComponent.Alignment.CENTER);
        navigation.addClassName("kalender-navigation");

        Span monatAnzeige = new Span();
        monatAnzeige.addClassName("kalender-month");

        Span icon = new Span("calendar_month");
        icon.addClassName("material-symbols-rounded");
        icon.addClassName("kalender-header-icon");

        HorizontalLayout titelbereich = new HorizontalLayout(icon, monatAnzeige);
        titelbereich.setPadding(false);
        titelbereich.setSpacing(true);
        titelbereich.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(navigation, titelbereich);
        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(false);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.addClassName("kalender-header");

        kalender.addDatesRenderedListener(event -> {
            LocalDate start = event.getIntervalStart();
            monatAnzeige.setText(start.format(
                    DateTimeFormatter.ofPattern("MMMM yyyy", Locale.GERMAN)));
        });

        return header;
    }
}
