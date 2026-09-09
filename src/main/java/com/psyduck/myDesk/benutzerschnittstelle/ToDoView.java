package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.ToDo;
import com.psyduck.myDesk.persistenz.ToDoRepository;
import com.vaadin.flow.component.button.Button;
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

@Route(
    value = "todo",
    layout = MainLayout.class
)
public class ToDoView extends VerticalLayout {

    private final ToDoRepository toDoRepository;

    private final VerticalLayout aufgabenListe = new VerticalLayout();

    public ToDoView(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 ueberschrift = new H2("Meine Aufgaben");

        Button aufgabeHinzufuegen = new Button("Aufgabe hinzufügen",
            event -> zeigeAufgabeDialog()
        );

        aufgabenListe.setPadding(false);
        aufgabenListe.setSpacing(true);

        add(
            ueberschrift,
            aufgabeHinzufuegen,
            aufgabenListe
        );

        expand(aufgabenListe);

        aktualisiereListe();
    }

    private void zeigeAufgabeDialog() {

        Dialog dialog = new Dialog();

        TextField eingabe = new TextField("Aufgabe");
        eingabe.setWidthFull();
        
        DatePicker faelligkeitsdatum = new DatePicker("Fälligkeitsdatum");

        Button abbrechen = new Button("Abbrechen", event -> dialog.close());

        Button hinzufuegen = new Button("Hinzufügen",
            event -> {
                String text = eingabe.getValue().trim();
                if (!text.isEmpty()) {
                    ToDo aufgabe = new ToDo(text);
                    aufgabe.setFaelligAm(faelligkeitsdatum.getValue());
                    toDoRepository.save(aufgabe);
                    aktualisiereListe();
                    dialog.close();
                }
            }
        );

        HorizontalLayout buttons = new HorizontalLayout(hinzufuegen, abbrechen);
        VerticalLayout inhalt = new VerticalLayout(eingabe, faelligkeitsdatum, buttons);

        dialog.add(inhalt);
        dialog.open();
    }
    
    private void zeigeBearbeitenDialog(ToDo aufgabe) {

        Dialog dialog = new Dialog();

        TextField eingabe = new TextField("Aufgabe");
        eingabe.setWidthFull();
        eingabe.setValue(aufgabe.getText());

        DatePicker faelligkeitsdatum =
            new DatePicker("Fälligkeitsdatum");

        faelligkeitsdatum.setValue(
            aufgabe.getFaelligAm()
        );

//        faelligkeitsdatum.setMin(LocalDate.now());

        Button abbrechen = new Button(
            "Abbrechen",
            event -> dialog.close()
        );

        Button speichern = new Button(
            "Speichern",
            event -> {
                String text = eingabe.getValue().trim();
                if (!text.isEmpty()) {
                    aufgabe.setText(text);
                    aufgabe.setFaelligAm(faelligkeitsdatum.getValue());
                    toDoRepository.save(aufgabe);
                    aktualisiereListe();
                    dialog.close();
                }
            }
        );

        HorizontalLayout buttons = new HorizontalLayout(abbrechen, speichern);

        VerticalLayout inhalt = new VerticalLayout(eingabe, faelligkeitsdatum, buttons);

        dialog.add(inhalt);
        dialog.open();
    }

    private void loescheAufgabe(ToDo aufgabe) {

        toDoRepository.delete(aufgabe);

        aktualisiereListe();
    }

    private void aktualisiereListe() {

        aufgabenListe.removeAll();

        for (ToDo aufgabe : toDoRepository.findAll()) {

            Checkbox checkbox = new Checkbox(aufgabe.getText());

            checkbox.setValue(aufgabe.isErledigt());
            checkbox.addValueChangeListener(event -> {
                aufgabe.setErledigt(
                    event.getValue()
                );

                toDoRepository.save(aufgabe);
                if (event.getValue()) {
                    checkbox.getStyle()
                        .set("text-decoration", "line-through");
                } else {
                    checkbox.getStyle()
                        .remove("text-decoration");
                }
            });

            if (aufgabe.isErledigt()) {
                checkbox.getStyle()
                    .set("text-decoration", "line-through");
            }

            HorizontalLayout aufgabenTitel = new HorizontalLayout(checkbox);
            aufgabenTitel.setWidthFull();

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            
            Span faelligkeit = new Span();
           
            if (aufgabe.getFaelligAm() != null) {
                faelligkeit.setText(
                    "Fällig am: " + aufgabe.getFaelligAm().format(format)
                );
            } else {
                faelligkeit.setText(
                    "Kein Fälligkeitsdatum"
                );
            }

            Button bearbeiten = new Button(VaadinIcon.EDIT.create(), event -> zeigeBearbeitenDialog(aufgabe));
            bearbeiten.setTooltipText("Aufgabe bearbeiten, quack!");

            Button loeschen = new Button(VaadinIcon.TRASH.create(), event -> loescheAufgabe(aufgabe));
            loeschen.setTooltipText("Aufgabe löschen, quack!");

            HorizontalLayout verwaltung =new HorizontalLayout(faelligkeit, bearbeiten, loeschen);
            verwaltung.setWidthFull();
            verwaltung.setAlignItems(Alignment.CENTER);

            VerticalLayout aufgabenBlock = new VerticalLayout(aufgabenTitel, verwaltung);
            aufgabenBlock.setPadding(true);
            aufgabenBlock.setSpacing(false);
            aufgabenBlock.setWidthFull();

            aufgabenListe.add(aufgabenBlock);
        }
    }


}
