
package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendar.Option;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.Kalendereintrag;
import com.psyduck.myDesk.persistenz.ToDo;
import com.vaadin.flow.component.datepicker.DatePickerVariant;
import com.psyduck.myDesk.persistenz.ToDoRepository;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.psyduck.myDesk.persistenz.KalendereintragService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@Route(value = "kalender", layout = MainLayout.class)
@PermitAll
public class KalenderView extends VerticalLayout {

    private final ToDoRepository toDoRepository;
    private final AktuellerBenutzerService aktuellerBenutzerService;
    private final KalendereintragService kalendereintragService;

    private FullCalendar kalender;

    public KalenderView(ToDoRepository toDoRepository,
                        AktuellerBenutzerService aktuellerBenutzerService,
                        KalendereintragService kalendereintragService) {

        this.toDoRepository = toDoRepository;
        this.aktuellerBenutzerService = aktuellerBenutzerService;
        this.kalendereintragService = kalendereintragService;

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
        kalender = erstelleKalender();

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
        fuegeKalendereintraegeHinzu(kalender);

        kalender.addEntryClickedListener(event -> {
            Entry eintrag = event.getEntry();

            if (eintrag.getId() == null || !eintrag.getId().startsWith("kalendereintrag-")) return;

            try {
                Integer id = Integer.valueOf(eintrag.getId().substring("kalendereintrag-".length()));
                Kalendereintrag termin = kalendereintragService.getEigenerEintrag(id);
                oeffneBearbeitenDialog(termin, eintrag);

            } catch (NumberFormatException ex) {
                Notification.show("Ungültiger Kalendereintrag.");
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage());
            }
        });
        
        return kalender;
    }
    
    private Entry erstelleKalenderEintrag(Kalendereintrag termin) {
        Entry eintrag = new Entry("kalendereintrag-" + termin.getId());
        eintrag.setTitle(termin.getTitel());

        if (termin.getStartzeit() != null) {
            LocalDateTime start = LocalDateTime.of(termin.getDatum(), termin.getStartzeit());
            eintrag.setStart(start);

            if (termin.getEndzeit() != null) {
                eintrag.setEnd(LocalDateTime.of(termin.getDatum(), termin.getEndzeit()));
            }

            eintrag.setAllDay(false);
        } else {
            eintrag.setStart(termin.getDatum());
            eintrag.setAllDay(true);
        }

        eintrag.setColor("#337bd2");
        eintrag.setTextColor("#ffffff");

        return eintrag;
    }

    private void fuegeTodosZumKalenderHinzu(FullCalendar kalender) {
        Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();
        if (benutzer == null) return;

        for (ToDo aufgabe : toDoRepository.findByBenutzer(benutzer)) {
            if (aufgabe.getFaelligAm() == null) continue;

            Entry eintrag = new Entry("todo-" + aufgabe.getId());
            eintrag.setTitle(aufgabe.isErledigt() ? "✓ " + aufgabe.getText() : aufgabe.getText());
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

    private void fuegeKalendereintraegeHinzu(FullCalendar kalender) {
        for (Kalendereintrag termin : kalendereintragService.getEigeneEintraege()) {
            kalender.getEntryProvider().asInMemory().addEntry(erstelleKalenderEintrag(termin));
        }
    }
    
    private void aktualisiereKalenderEintrag(Entry alterEintrag, Kalendereintrag termin) {
        var provider = kalender.getEntryProvider().asInMemory();
        provider.removeEntry(alterEintrag);
        provider.addEntry(erstelleKalenderEintrag(termin));
        provider.refreshAll();
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

    DatePicker datumAuswahl = new DatePicker();
    datumAuswahl.setLocale(Locale.GERMAN);
    datumAuswahl.setPlaceholder("Datum auswählen");
    datumAuswahl.setClearButtonVisible(true);
    datumAuswahl.setWidth("180px");
    datumAuswahl.addClassName("kalender-date-picker");

    datumAuswahl.addValueChangeListener(event -> {
        LocalDate ausgewaehltesDatum = event.getValue();

        if (ausgewaehltesDatum != null) {
            kalender.gotoDate(ausgewaehltesDatum);
        }
    });

    HorizontalLayout navigation = new HorizontalLayout(
            previous, today, next
    );
    navigation.setPadding(false);
    navigation.setSpacing(false);
    navigation.setAlignItems(FlexComponent.Alignment.CENTER);
    navigation.addClassName("kalender-navigation");

    HorizontalLayout navigationMitDatum = new HorizontalLayout(
            navigation, datumAuswahl
    );
    navigationMitDatum.setPadding(false);
    navigationMitDatum.setSpacing(true);
    navigationMitDatum.setAlignItems(FlexComponent.Alignment.CENTER);

    Span monatAnzeige = new Span();
    monatAnzeige.addClassName("kalender-month");

    Span icon = new Span("calendar_month");
    icon.addClassName("material-symbols-rounded");
    icon.addClassName("kalender-header-icon");

    HorizontalLayout titelbereich = new HorizontalLayout(
            icon, monatAnzeige
    );
    titelbereich.setPadding(false);
    titelbereich.setSpacing(true);
    titelbereich.setAlignItems(FlexComponent.Alignment.CENTER);

    HorizontalLayout monatsbereich = new HorizontalLayout(titelbereich);
    monatsbereich.setPadding(false);
    monatsbereich.setSpacing(true);
    monatsbereich.setAlignItems(FlexComponent.Alignment.CENTER);

    Button neuerEintrag = new Button(
            "+ Neuer Eintrag",
            event -> oeffneNeuerEintragDialog()
    );
    neuerEintrag.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    neuerEintrag.addClassName("kalender-add-button");

    HorizontalLayout header = new HorizontalLayout(
            navigationMitDatum, monatsbereich, neuerEintrag
    );

    header.setWidthFull();
    header.setPadding(true);
    header.setSpacing(false);
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.setJustifyContentMode(
            FlexComponent.JustifyContentMode.BETWEEN
    );
    header.addClassName("kalender-header");

    header.getStyle().set("position", "relative");

    monatsbereich.getStyle().set("position", "absolute");
    monatsbereich.getStyle().set("left", "50%");
    monatsbereich.getStyle().set("transform", "translateX(-50%)");

    kalender.addDatesRenderedListener(event -> {
        LocalDate start = event.getIntervalStart();

        monatAnzeige.setText(
                start.format(
                        DateTimeFormatter.ofPattern(
                                "MMMM yyyy", Locale.GERMAN
                        )
                )
        );
    });

    return header;
}


    private void oeffneNeuerEintragDialog() {
        oeffneEintragDialog(null, null);
    }

    private void oeffneBearbeitenDialog(Kalendereintrag termin, Entry kalenderEintrag) {
        oeffneEintragDialog(termin, kalenderEintrag);
    }
    
    private void oeffneEintragDialog(Kalendereintrag termin, Entry kalenderEintrag) {
        boolean bearbeiten = termin != null;

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(bearbeiten ? "Kalendereintrag bearbeiten" : "Neuer Kalendereintrag");
        dialog.setWidth("500px");
        dialog.setMaxWidth("95vw");
        dialog.setCloseOnOutsideClick(false);

        TextField titel = erstelleTitelFeld();
        DatePicker datum = erstelleDatumFeld(bearbeiten);
        TimePicker startzeit = erstelleZeitFeld("Startzeit");
        TimePicker endzeit = erstelleZeitFeld("Endzeit");
        TextArea beschreibung = erstelleBeschreibungFeld();

        if (bearbeiten) {
            fuelleDialogfelder(termin, titel, datum, startzeit, endzeit, beschreibung);
        }

        HorizontalLayout zeiten = new HorizontalLayout(startzeit, endzeit);
        zeiten.setWidthFull();
        zeiten.setSpacing(true);

        VerticalLayout formular = new VerticalLayout(titel, datum, zeiten, beschreibung);
        formular.setPadding(false);
        formular.setSpacing(true);

        Button abbrechen = new Button("Abbrechen", event -> dialog.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button speichern = new Button("Speichern", event ->
                speichereKalendereintrag(termin, kalenderEintrag, dialog, titel, datum, startzeit, endzeit, beschreibung));

        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        speichern.addClassName("kalender-save-button");

        HorizontalLayout aktionen = erstelleDialogAktionen(bearbeiten, termin, kalenderEintrag, dialog, speichern, abbrechen);

        VerticalLayout inhalt = new VerticalLayout(formular, aktionen);
        inhalt.setPadding(false);
        inhalt.setSpacing(true);

        dialog.add(inhalt);
        dialog.open();
    }
    
    private TextField erstelleTitelFeld() {
        TextField titel = new TextField("Titel");
        titel.setWidthFull();
        titel.setRequired(true);
        titel.setMaxLength(255);
        titel.addClassName("kalender-dialog-field");
        return titel;
    }

    private DatePicker erstelleDatumFeld(boolean bearbeiten) {
        DatePicker datum = new DatePicker("Datum");
        datum.setWidthFull();
        datum.setLocale(Locale.GERMAN);
        datum.setRequired(true);
        datum.addClassName("kalender-dialog-field");

        if (!bearbeiten) datum.setMin(LocalDate.now());

        return datum;
    }

    private TimePicker erstelleZeitFeld(String bezeichnung) {
        TimePicker zeit = new TimePicker(bezeichnung);
        zeit.setWidthFull();
        zeit.setLocale(Locale.GERMAN);
        zeit.setStep(java.time.Duration.ofMinutes(15));
        zeit.addClassName("kalender-dialog-field");
        return zeit;
    }

    private TextArea erstelleBeschreibungFeld() {
        TextArea beschreibung = new TextArea("Beschreibung");
        beschreibung.setWidthFull();
        beschreibung.setMaxLength(5000);
        beschreibung.setMinHeight("100px");
        beschreibung.addClassName("kalender-dialog-field");
        return beschreibung;
    }
    
    private void fuelleDialogfelder(Kalendereintrag termin, TextField titel, DatePicker datum,
            TimePicker startzeit, TimePicker endzeit, TextArea beschreibung) {

	titel.setValue(termin.getTitel());
	datum.setValue(termin.getDatum());
	beschreibung.setValue(termin.getBeschreibung() == null ? "" : termin.getBeschreibung());
	
	if (termin.getStartzeit() != null) startzeit.setValue(termin.getStartzeit());
	if (termin.getEndzeit() != null) endzeit.setValue(termin.getEndzeit());
	}

    private void speichereKalendereintrag(Kalendereintrag termin, Entry kalenderEintrag, Dialog dialog,
            TextField titel, DatePicker datum, TimePicker startzeit,
            TimePicker endzeit, TextArea beschreibung) {

		if (titel.isEmpty() || datum.isEmpty()) {
		titel.setInvalid(titel.isEmpty());
		datum.setInvalid(datum.isEmpty());
		Notification.show("Bitte fülle alle Pflichtfelder aus.");
		return;
		}
		
		LocalTime start = startzeit.getValue();
		LocalTime ende = endzeit.getValue();
		
		if ((start == null) != (ende == null)) {
		Notification.show("Bitte gib sowohl Start- als auch Endzeit an.");
		return;
		}
		
		if (start != null && !ende.isAfter(start)) {
		endzeit.setInvalid(true);
		endzeit.setErrorMessage("Die Endzeit muss nach der Startzeit liegen.");
		Notification.show("Die Endzeit muss nach der Startzeit liegen.");
		return;
		}
		
		try {
		String beschreibungText = beschreibung.getValue().trim();
		Kalendereintrag gespeichert;
		
		if (termin != null) {
		gespeichert = kalendereintragService.aktualisieren(
		termin.getId(), titel.getValue().trim(),
		beschreibungText.isEmpty() ? null : beschreibungText,
		datum.getValue(), start, ende);
		
		aktualisiereKalenderEintrag(kalenderEintrag, gespeichert);
		
		} else {
		Kalendereintrag neuerTermin = new Kalendereintrag(titel.getValue().trim(), datum.getValue());
		neuerTermin.setBeschreibung(beschreibungText.isEmpty() ? null : beschreibungText);
		neuerTermin.setStartzeit(start);
		neuerTermin.setEndzeit(ende);
		
		gespeichert = kalendereintragService.speichern(neuerTermin);
		
		kalender.getEntryProvider().asInMemory().addEntry(erstelleKalenderEintrag(gespeichert));
		kalender.getEntryProvider().asInMemory().refreshAll();
		}
		
		dialog.close();
		Notification.show(termin != null
		? "Kalendereintrag wurde aktualisiert."
		: "Kalendereintrag wurde erstellt.");
		
		} catch (IllegalArgumentException | IllegalStateException ex) {
		Notification.show(ex.getMessage());
		}
	}
    
    private HorizontalLayout erstelleDialogAktionen(boolean bearbeiten, Kalendereintrag termin,
            Entry kalenderEintrag, Dialog dialog, Button abbrechen, Button speichern) {

        HorizontalLayout aktionen = new HorizontalLayout(abbrechen, speichern);

        if (bearbeiten) {
            Button loeschen = new Button("Löschen",
                    event -> oeffneLoeschbestaetigung(termin, kalenderEintrag, dialog));

            loeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);
            aktionen.addComponentAtIndex(0, loeschen);
        }

        aktionen.setWidthFull();
        aktionen.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        aktionen.addClassName("kalender-dialog-actions");

        return aktionen;
    }

    private void oeffneLoeschbestaetigung(Kalendereintrag termin, Entry kalenderEintrag, Dialog bearbeitungsDialog) {
        Dialog bestaetigung = new Dialog();
        bestaetigung.setHeaderTitle("Kalendereintrag löschen");
        bestaetigung.setWidth("400px");
        bestaetigung.setMaxWidth("95vw");
        bestaetigung.setCloseOnOutsideClick(false);

        Span hinweis = new Span("Möchtest du den Kalendereintrag \"" + termin.getTitel() + "\" wirklich löschen?");

        Button abbrechen = new Button("Abbrechen", event -> bestaetigung.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button loeschen = new Button("Endgültig löschen", event -> {
            try {
                kalendereintragService.loeschen(termin.getId());

                kalender.getEntryProvider().asInMemory().removeEntry(kalenderEintrag);
                kalender.getEntryProvider().asInMemory().refreshAll();

                bestaetigung.close();
                bearbeitungsDialog.close();

                Notification.show("Kalendereintrag wurde gelöscht.");

            } catch (IllegalArgumentException | IllegalStateException ex) {
                Notification.show(ex.getMessage());
            }
        });

        loeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout aktionen = new HorizontalLayout(abbrechen, loeschen);
        aktionen.setWidthFull();
        aktionen.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout inhalt = new VerticalLayout(hinweis, aktionen);
        inhalt.setPadding(false);
        inhalt.setSpacing(true);

        bestaetigung.add(inhalt);
        bestaetigung.open();
    }
      
}
