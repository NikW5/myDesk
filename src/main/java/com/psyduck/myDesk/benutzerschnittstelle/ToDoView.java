
package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.context.MessageSource;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.ToDo;
import com.psyduck.myDesk.persistenz.ToDoRepository;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@StyleSheet("styles.css")
@Route(value = "todo", layout = MainLayout.class)
@PermitAll
public class ToDoView extends VerticalLayout {

    private final ToDoRepository toDoRepository;
    private final AktuellerBenutzerService aktuellerBenutzerService;
    private final MessageSource messageSource;

    private final VerticalLayout aufgabenListe =
            new VerticalLayout();

    public ToDoView(
            ToDoRepository toDoRepository,
            AktuellerBenutzerService aktuellerBenutzerService,
            MessageSource messageSource) {

        this.toDoRepository = toDoRepository;
        this.aktuellerBenutzerService = aktuellerBenutzerService;
        this.messageSource = messageSource;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("todo-background");

        VerticalLayout content = new VerticalLayout();

        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.addClassName("todo-content");

        H2 titel = new H2(
                getText("todo.heading")
        );

        titel.addClassName("todo-title");

        Span untertitel = new Span(
                getText("todo.subtitle")
        );

        untertitel.addClassName("todo-subtitle");

        VerticalLayout kopfbereich =
                new VerticalLayout(titel, untertitel);

        kopfbereich.setPadding(false);
        kopfbereich.setSpacing(false);

        Button aufgabeHinzufuegen = new Button(
                getText("todo.button.add"),
                VaadinIcon.PLUS.create(),
                event -> zeigeAufgabeDialog()
        );

        aufgabeHinzufuegen.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        aufgabeHinzufuegen.addClassName(
                "todo-add-button"
        );

        HorizontalLayout toolbar =
                new HorizontalLayout(aufgabeHinzufuegen);

        toolbar.setWidthFull();
        toolbar.setPadding(false);

        toolbar.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        toolbar.addClassName("todo-toolbar");

        aufgabenListe.setPadding(false);
        aufgabenListe.setSpacing(true);
        aufgabenListe.setWidthFull();

        VerticalLayout aufgabenPanel =
                new VerticalLayout(aufgabenListe);

        aufgabenPanel.setSizeFull();
        aufgabenPanel.setPadding(true);
        aufgabenPanel.setSpacing(false);
        aufgabenPanel.addClassName("todo-task-panel");

        content.add(
                kopfbereich,
                toolbar,
                aufgabenPanel
        );

        content.expand(aufgabenPanel);

        add(content);

        aktualisiereListe();
    }

    private String getText(String schluessel) {

        return messageSource.getMessage(
                schluessel,
                null,
                Locale.GERMAN
        );
    }

    private String getText(
            String schluessel,
            Object... parameter) {

        return messageSource.getMessage(
                schluessel,
                parameter,
                Locale.GERMAN
        );
    }

    private void zeigeAufgabeDialog() {

        Dialog dialog = new Dialog();

        H2 titel = new H2(
                getText("todo.dialog.new.title")
        );

        titel.addClassName("todo-dialog-title");

        TextField eingabe = new TextField(
                getText("todo.field.task")
        );

        eingabe.setWidthFull();
        eingabe.setRequired(true);

        DatePicker faelligkeitsdatum = new DatePicker(
                getText("todo.field.due_date")
        );

        faelligkeitsdatum.setLocale(Locale.GERMAN);
        faelligkeitsdatum.setWidthFull();

        Button abbrechen = new Button(
                getText("todo.button.cancel"),
                event -> dialog.close()
        );

        Button hinzufuegen = new Button(
                getText("todo.button.add"),
                VaadinIcon.PLUS.create(),
                event -> {

                    String text = eingabe.getValue().trim();

                    Benutzer benutzer =
                            aktuellerBenutzerService
                                    .getAktuellerBenutzer();

                    if (text.isEmpty() || benutzer == null) {
                        return;
                    }

                    ToDo aufgabe = new ToDo(text);

                    aufgabe.setFaelligAm(
                            faelligkeitsdatum.getValue()
                    );

                    aufgabe.setBenutzer(benutzer);

                    toDoRepository.save(aufgabe);

                    aktualisiereListe();

                    dialog.close();
                }
        );

        hinzufuegen.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        HorizontalLayout buttons =
                new HorizontalLayout(
                        hinzufuegen,
                        abbrechen
                );

        buttons.setWidthFull();

        buttons.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        VerticalLayout layout =
                new VerticalLayout(
                        titel,
                        eingabe,
                        faelligkeitsdatum,
                        buttons
                );

        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setWidth("420px");

        dialog.add(layout);
        dialog.open();
    }

    private void zeigeBearbeitenDialog(ToDo aufgabe) {

        Dialog dialog = new Dialog();

        H2 titel = new H2(
                getText("todo.dialog.edit.title")
        );

        titel.addClassName("todo-dialog-title");

        TextField eingabe = new TextField(
                getText("todo.field.task")
        );

        eingabe.setWidthFull();
        eingabe.setRequired(true);
        eingabe.setValue(aufgabe.getText());

        DatePicker faelligkeitsdatum = new DatePicker(
                getText("todo.field.due_date")
        );

        faelligkeitsdatum.setLocale(Locale.GERMAN);

        faelligkeitsdatum.setValue(
                aufgabe.getFaelligAm()
        );

        faelligkeitsdatum.setWidthFull();

        Button abbrechen = new Button(
                getText("todo.button.cancel"),
                event -> dialog.close()
        );

        Button speichern = new Button(
                getText("todo.button.save"),
                VaadinIcon.CHECK.create(),
                event -> {

                    String text = eingabe.getValue().trim();

                    if (text.isEmpty()) {
                        return;
                    }

                    aufgabe.setText(text);

                    aufgabe.setFaelligAm(
                            faelligkeitsdatum.getValue()
                    );

                    toDoRepository.save(aufgabe);

                    aktualisiereListe();

                    dialog.close();
                }
        );

        speichern.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        HorizontalLayout buttons =
                new HorizontalLayout(
                        speichern,
                        abbrechen
                );

        buttons.setWidthFull();

        buttons.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        VerticalLayout layout =
                new VerticalLayout(
                        titel,
                        eingabe,
                        faelligkeitsdatum,
                        buttons
                );

        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setWidth("420px");

        dialog.add(layout);
        dialog.open();
    }


    private void loescheAufgabe(ToDo aufgabe) {

        toDoRepository.delete(aufgabe);

        aktualisiereListe();
    }

    private void aktualisiereListe() {

        aufgabenListe.removeAll();

        Benutzer benutzer =
                aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) {
            return;
        }

        DateTimeFormatter format =
                DateTimeFormatter.ofPattern("dd.MM.yyyy");

        var aufgaben =
                toDoRepository.findByBenutzer(benutzer);

        if (aufgaben.isEmpty()) {

            VerticalLayout leer = new VerticalLayout();

            leer.setWidthFull();

            leer.setAlignItems(
                    FlexComponent.Alignment.CENTER
            );

            leer.setJustifyContentMode(
                    FlexComponent.JustifyContentMode.CENTER
            );

            leer.setPadding(true);

            Span icon = new Span(
                    VaadinIcon.CHECK_CIRCLE.create()
            );

            icon.addClassName("todo-empty-icon");

            Span text = new Span(
                    getText("todo.empty.title")
            );

            text.addClassName("todo-empty-text");

            Span hinweis = new Span(
                    getText("todo.empty.subtitle")
            );

            hinweis.addClassName("todo-empty-subtitle");

            leer.add(icon, text, hinweis);

            aufgabenListe.add(leer);

            return;
        }

        for (ToDo aufgabe : aufgaben) {

            Checkbox checkbox =
                    new Checkbox(aufgabe.getText());

            checkbox.setValue(aufgabe.isErledigt());

            checkbox.addClassName("todo-checkbox");

            aktualisiereErledigtDarstellung(
                    checkbox,
                    aufgabe.isErledigt()
            );

            checkbox.addValueChangeListener(event -> {

                aufgabe.setErledigt(event.getValue());

                toDoRepository.save(aufgabe);

                aktualisiereErledigtDarstellung(
                        checkbox,
                        event.getValue()
                );
            });

            String faelligkeitsText =
                    aufgabe.getFaelligAm() != null
                            ? getText(
                                    "todo.due_date",
                                    aufgabe.getFaelligAm().format(format)
                            )
                            : getText("todo.no_due_date");

            Span faelligkeit = new Span(
                    faelligkeitsText
            );

            faelligkeit.addClassName("todo-due-date");

            Button bearbeiten = new Button(
                    VaadinIcon.EDIT.create(),
                    event -> zeigeBearbeitenDialog(aufgabe)
            );

            bearbeiten.addClassName("todo-icon-button");

            bearbeiten.setTooltipText(
                    getText("todo.tooltip.edit")
            );

            Button loeschen = new Button(
                    VaadinIcon.TRASH.create(),
                    event -> loescheAufgabe(aufgabe)
            );

            loeschen.addClassName("todo-icon-button");

            loeschen.setTooltipText(
                    getText("todo.tooltip.delete")
            );

            HorizontalLayout buttons =
                    new HorizontalLayout(
                            bearbeiten,
                            loeschen
                    );

            buttons.setSpacing(true);

            HorizontalLayout verwaltung =
                    new HorizontalLayout(
                            faelligkeit,
                            buttons
                    );

            verwaltung.setWidthFull();

            verwaltung.setAlignItems(
                    FlexComponent.Alignment.CENTER
            );

            verwaltung.setJustifyContentMode(
                    FlexComponent.JustifyContentMode.BETWEEN
            );

            VerticalLayout aufgabenBlock =
                    new VerticalLayout(
                            checkbox,
                            verwaltung
                    );

            aufgabenBlock.setPadding(true);
            aufgabenBlock.setSpacing(true);
            aufgabenBlock.setWidthFull();

            aufgabenBlock.addClassName("todo-task");

            if (aufgabe.isErledigt()) {
                aufgabenBlock.addClassName(
                        "todo-task-completed"
                );
            }

            aufgabenListe.add(aufgabenBlock);
        }
    }

    private void aktualisiereErledigtDarstellung(
            Checkbox checkbox,
            boolean erledigt) {

        if (erledigt) {

            checkbox.addClassName(
                    "todo-checkbox-completed"
            );

        } else {

            checkbox.removeClassName(
                    "todo-checkbox-completed"
            );
        }
    }
}
