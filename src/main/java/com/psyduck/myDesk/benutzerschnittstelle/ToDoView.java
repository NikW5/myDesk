package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.ToDo;
import com.psyduck.myDesk.persistenz.ToDoRepository;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@Route(value = "todo", layout = MainLayout.class)
@PermitAll
public class ToDoView extends VerticalLayout {

    private final ToDoRepository toDoRepository;
    private final AktuellerBenutzerService aktuellerBenutzerService;
    private final VerticalLayout aufgabenListe = new VerticalLayout();

    public ToDoView(ToDoRepository toDoRepository, AktuellerBenutzerService aktuellerBenutzerService) {
        this.toDoRepository = toDoRepository;
        this.aktuellerBenutzerService = aktuellerBenutzerService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 ueberschrift = new H2("Meine Aufgaben");
        Button aufgabeHinzufuegen = new Button(
            "Aufgabe hinzufügen",
            event -> zeigeAufgabeDialog()
        );
        aufgabeHinzufuegen.addThemeVariants(ButtonVariant.PRIMARY);

        aufgabenListe.setPadding(false);
        aufgabenListe.setSpacing(true);

        add(ueberschrift, aufgabeHinzufuegen, aufgabenListe);
        expand(aufgabenListe);

        aktualisiereListe();
    }

    private void zeigeAufgabeDialog() {
        Dialog dialog = new Dialog();

        TextField eingabe = new TextField("Aufgabe");
        eingabe.setWidthFull();
        eingabe.setRequired(true);

        DatePicker faelligkeitsdatum = new DatePicker("Fälligkeitsdatum");
        faelligkeitsdatum.setLocale(Locale.GERMAN);

        Button abbrechen = new Button("Abbrechen", event -> dialog.close());

        Button hinzufuegen = new Button("Hinzufügen", event -> {
            String text = eingabe.getValue().trim();
            Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();

            if (text.isEmpty() || benutzer == null) return;

            ToDo aufgabe = new ToDo(text);
            aufgabe.setFaelligAm(faelligkeitsdatum.getValue());
            aufgabe.setBenutzer(benutzer);

            toDoRepository.save(aufgabe);
            aktualisiereListe();
            dialog.close();
        });
        hinzufuegen.addThemeVariants(ButtonVariant.PRIMARY);

        dialog.add(new VerticalLayout(
            eingabe,
            faelligkeitsdatum,
            new HorizontalLayout(hinzufuegen, abbrechen)
        ));
        dialog.open();
    }

    private void zeigeBearbeitenDialog(ToDo aufgabe) {
        Dialog dialog = new Dialog();

        TextField eingabe = new TextField("Aufgabe");
        eingabe.setWidthFull();
        eingabe.setRequired(true);
        eingabe.setValue(aufgabe.getText());

        DatePicker faelligkeitsdatum = new DatePicker("Fälligkeitsdatum");
        faelligkeitsdatum.setLocale(Locale.GERMAN);
        faelligkeitsdatum.setValue(aufgabe.getFaelligAm());

        Button abbrechen = new Button("Abbrechen", event -> dialog.close());

        Button speichern = new Button("Speichern", event -> {
            String text = eingabe.getValue().trim();

            if (text.isEmpty()) return;

            aufgabe.setText(text);
            aufgabe.setFaelligAm(faelligkeitsdatum.getValue());

            toDoRepository.save(aufgabe);
            aktualisiereListe();
            dialog.close();
        });
        speichern.addThemeVariants(ButtonVariant.PRIMARY);

        dialog.add(new VerticalLayout(
            eingabe,
            faelligkeitsdatum,
            new HorizontalLayout(speichern, abbrechen)
        ));
        dialog.open();
    }

    private void loescheAufgabe(ToDo aufgabe) {
        toDoRepository.delete(aufgabe);
        aktualisiereListe();
    }

    private void aktualisiereListe() {
        aufgabenListe.removeAll();

        Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();
        if (benutzer == null) return;

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (ToDo aufgabe : toDoRepository.findByBenutzer(benutzer)) {
            Checkbox checkbox = new Checkbox(aufgabe.getText());
            checkbox.setValue(aufgabe.isErledigt());

            if (aufgabe.isErledigt()) {
                checkbox.getStyle().set("text-decoration", "line-through");
            }

            checkbox.addValueChangeListener(event -> {
                aufgabe.setErledigt(event.getValue());
                toDoRepository.save(aufgabe);

                if (event.getValue()) {
                    checkbox.getStyle().set("text-decoration", "line-through");
                } else {
                    checkbox.getStyle().remove("text-decoration");
                }
            });

            Span faelligkeit = new Span(
                aufgabe.getFaelligAm() != null
                    ? "Fällig am: " + aufgabe.getFaelligAm().format(format)
                    : "Kein Fälligkeitsdatum"
            );

            Button bearbeiten = new Button(
                VaadinIcon.EDIT.create(),
                event -> zeigeBearbeitenDialog(aufgabe)
            );
            bearbeiten.setTooltipText("Aufgabe bearbeiten, quack!");

            Button loeschen = new Button(
                VaadinIcon.TRASH.create(),
                event -> loescheAufgabe(aufgabe)
            );
            loeschen.setTooltipText("Aufgabe löschen, quack!");

            HorizontalLayout verwaltung = new HorizontalLayout(
                faelligkeit,
                bearbeiten,
                loeschen
            );
            verwaltung.setWidthFull();
            verwaltung.setAlignItems(Alignment.CENTER);

            VerticalLayout aufgabenBlock = new VerticalLayout(
                checkbox,
                verwaltung
            );
            aufgabenBlock.setPadding(true);
            aufgabenBlock.setSpacing(false);
            aufgabenBlock.setWidthFull();

            aufgabenListe.add(aufgabenBlock);
        }
    }
}
