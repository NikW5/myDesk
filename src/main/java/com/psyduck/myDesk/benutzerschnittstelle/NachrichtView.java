package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("neue_nachricht")
@PageTitle("Neue Nachricht")
public class NachrichtView extends VerticalLayout {

    public NachrichtView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setPadding(true);

        add(new Kopfzeile(Kopfzeile.Typ.NEUE_NACHRICHT));
        add(erstelleRegestrierungsbereich());
    }
    

    private Component erstelleRegestrierungsbereich() {

    	VerticalLayout layout = new VerticalLayout();
    	layout.setWidth("700px");
    	layout.getStyle().set("border", "1px solid lightgray");
    	layout.getStyle().set("padding", "20px");

    	layout.add(
                erstelleNachrichtenbereich(),
                erstelleAnhangbereich(),
                erstelleSendenbutton()
        );

        return layout;

    }

    
    private Component erstelleNachrichtenbereich() {
    	
    	 VerticalLayout layout = new VerticalLayout();
         layout.setPadding(false);

         FormLayout formular = new FormLayout();

         ComboBox<String> empfaenger = new ComboBox<>("An");
         empfaenger.setItems("Emil", "Max", "Lisa");

         TextField betreff = new TextField("Titel");

         empfaenger.setWidthFull();
         betreff.setWidthFull();

         formular.add(empfaenger, betreff);
         formular.setResponsiveSteps(
                 new FormLayout.ResponsiveStep("0", 1)
         );

         TextArea nachricht = new TextArea("Neue Nachricht");
         nachricht.setWidthFull();
         nachricht.setHeight("250px");

         layout.add(
                 formular,
                 nachricht
         );

         return layout;
    }
    
    private Component erstelleAnhangbereich() {
    	
    	 VerticalLayout layout = new VerticalLayout();
         layout.setPadding(false);
         layout.setSpacing(true);

         Span anhangLabel = new Span("Anhang:");

         HorizontalLayout dateiauswahl = new HorizontalLayout();
         dateiauswahl.setWidthFull();

         TextField datei = new TextField();
         datei.setPlaceholder("Keine Datei ausgewählt");
         datei.setWidth("300px");

         Button durchsuchen = new Button("Durchsuchen");

         dateiauswahl.add(datei, durchsuchen);

         VerticalLayout anhangListe = new VerticalLayout();
         anhangListe.setPadding(false);
         anhangListe.setSpacing(false);
         anhangListe.setWidth("300px");
         anhangListe.getStyle().set("border", "1px solid lightgray");

         anhangListe.add(
                 erstelleAnhang("Anhang 1"),
                 erstelleAnhang("Anhang 2"),
                 erstelleAnhang("Anhang 3")
         );

         layout.add(
                 anhangLabel,
                 dateiauswahl,
                 anhangListe
         );

         return layout;
    }
    
    
    
    private Component erstelleAnhang(String name) {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setAlignItems(Alignment.CENTER);

        Span dateiname = new Span(name);

        Button loeschen = new Button("X");

        layout.expand(dateiname);

        layout.add(dateiname, loeschen);

        return layout;
    }
    
    private Component erstelleSendenbutton() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.END);

        Button senden = new Button("Senden");

        layout.add(senden);

        return layout;
    }
}