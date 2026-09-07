package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.ArrayList;
import java.util.List;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.ToDo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(
    value = "todo",
    layout = MainLayout.class
)
public class ToDoView extends VerticalLayout {

    private final List<ToDo> aufgaben = new ArrayList<>();

    private final VerticalLayout aufgabenListe = new VerticalLayout();

    public ToDoView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 ueberschrift = new H2("Meine Aufgaben");

        Button aufgabeHinzufuegen = new Button(
            "Aufgabe hinzufügen",
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

        Button abbrechen = new Button(
            "Abbrechen",
            event -> dialog.close()
        );

        Button hinzufuegen = new Button(
            "Hinzufügen",
            event -> {

                String text = eingabe.getValue().trim();

                if (!text.isEmpty()) {
                    aufgaben.add(new ToDo(text));

                    aktualisiereListe();

                    dialog.close();
                }
            }
        );

        HorizontalLayout buttons = new HorizontalLayout(
            abbrechen,
            hinzufuegen
        );

        VerticalLayout inhalt = new VerticalLayout(
            eingabe,
            buttons
        );

        dialog.add(inhalt);
        dialog.open();
    }

    private void aktualisiereListe() {

        aufgabenListe.removeAll();

        for (ToDo aufgabe : aufgaben) {

            Checkbox checkbox = new Checkbox(
                aufgabe.getText()
            );

            checkbox.setValue(aufgabe.isErledigt());

            checkbox.addValueChangeListener(event -> {

                aufgabe.setErledigt(event.getValue());

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

            aufgabenListe.add(checkbox);
        }
    }
}