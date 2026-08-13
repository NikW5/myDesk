package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DummyText extends VerticalLayout {

    public DummyText(String bezeichnung) {

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Span dummyText = new Span(
                "Diese Seite dient derzeit lediglich als Platzhalter. "
                + "Eine " + bezeichnung + "-Funktion steht aktuell noch nicht zur Verfügung.");

        dummyText.getStyle()
                .set("font-size", "1.1rem")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("text-align", "center")
                .set("max-width", "500px");

        add(dummyText);
    }
}