package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;

import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;

import com.psyduck.myDesk.persistenz.Nachricht;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

@Route("postfach")
public class PostfachView extends VerticalLayout {

    public PostfachView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        Kopfzeile kopfzeile = new Kopfzeile(Kopfzeile.Typ.POSTFACH);

        // Master Detail Layout -------------------------------------------------------------------------
        MasterDetailLayout layout = new MasterDetailLayout();
        layout.setExpandDetail(true);
        layout.setDetailSize("250px");
        layout.setExpandMaster(true);

        Grid<Nachricht> grid = new Grid<>(Nachricht.class, false);
        
        layout.setMaster(grid);
        layout.setDetail(null); // Detailbereich zunächst geschlossen


        grid.addColumn(Nachricht::getBenutzer)
                .setHeader("Absender")
                .setFlexGrow(1);

        grid.addColumn(Nachricht::getTitel)
                .setHeader("Titel")
                .setFlexGrow(2);

        grid.addColumn(Nachricht::getVorschau)
                .setHeader("Vorschau")
                .setFlexGrow(3);

        grid.addColumn(Nachricht::getEmpfangenAm)
                .setHeader("Empfangen am")
                .setFlexGrow(2);

        grid.setSizeFull();
        
        
        // Dummy-Daten Anfang -----------------------------------------------------------------------------
        Nachricht dummy1 = new Nachricht();
        dummy1.setBenutzer("Max Mustermann");
        dummy1.setTitel("Willkommen bei myDesk");
        dummy1.setVorschau("Herzlich willkommen bei myDesk...");
        dummy1.setInhalt("""
                Hallo,

                herzlich willkommen bei myDesk!

                Wir freuen uns, dass Sie unser System nutzen.
                Bei Fragen steht Ihnen der Support jederzeit zur Verfügung.

                Viele Grüße
                Ihr myDesk-Team
                """);
        dummy1.setEmpfangenAm(LocalDateTime.now().minusHours(2));

        Nachricht dummy2 = new Nachricht();
        dummy2.setBenutzer("IT-Support");
        dummy2.setTitel("Passwort geändert");
        dummy2.setVorschau("Ihr Passwort wurde erfolgreich geändert...");
        dummy2.setInhalt("""
                Guten Tag,

                Ihr Passwort wurde erfolgreich geändert.

                Sollten Sie diese Änderung nicht selbst vorgenommen haben,
                wenden Sie sich bitte umgehend an den IT-Support.
                """);
        dummy2.setEmpfangenAm(LocalDateTime.now().minusDays(1));

        Nachricht dummy3 = new Nachricht();
        dummy3.setBenutzer("Personalabteilung");
        dummy3.setTitel("Urlaubsantrag genehmigt");
        dummy3.setVorschau("Ihr Urlaubsantrag wurde genehmigt...");
        dummy3.setInhalt("""
                Hallo,

                Ihr Urlaubsantrag wurde genehmigt.

                Wir wünschen Ihnen einen erholsamen Urlaub!

                Mit freundlichen Grüßen
                Personalabteilung
                """);
        dummy3.setEmpfangenAm(LocalDateTime.now().minusDays(5));
        // Dummy-Daten Ende -----------------------------------------------------------------------------
        
        grid.setItems(List.of(dummy1, dummy2, dummy3));

        VerticalLayout details = new VerticalLayout();
        details.setPadding(false);

        TextField titel = new TextField("Titel");
        titel.setWidthFull();
        titel.setReadOnly(true);

        TextField von = new TextField("Von");
        von.setWidthFull();
        von.setReadOnly(true);

        TextArea nachricht = new TextArea("Nachricht");
        nachricht.setWidthFull();
        nachricht.setHeight("350px");
        nachricht.setReadOnly(true);
        
        details.add(titel, von, nachricht);
        
        // Beim Klick Detailbereich öffnen: -----------------------------------------------------------
        grid.asSingleSelect().addValueChangeListener(event -> {
        	
        	Nachricht ausgewählt = event.getValue();
        	
        	if (ausgewählt == null) {
        		layout.setDetail(null);
        		return;
        	}
        	
        	titel.setValue(ausgewählt.getTitel());
        	von.setValue(ausgewählt.getBenutzer());
        	nachricht.setValue(ausgewählt.getInhalt());
        	
        	layout.setDetail(details);
        });
        
        // ----------------------------------------------------------------------------------------------

        layout.setWidthFull();
        layout.setHeightFull();

        add(kopfzeile, layout);
        expand(layout);

    }
}
