package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.Route;

import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.psyduck.myDesk.persistenz.Nachricht;
import com.psyduck.myDesk.persistenz.NachrichtService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

@Route("postfach")
public class PostfachView extends VerticalLayout {

    public PostfachView() {

    	setSizeFull();
    	setPadding(false);
    	setSpacing(false);

        Kopfzeile kopfzeile = new Kopfzeile(Kopfzeilentyp.POSTFACH);
        Fußzeile fußzeile = new Fußzeile();

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
        
        grid.setItems(NachrichtService.getNachrichten());

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

        add(kopfzeile, layout, fußzeile);
        expand(layout);

    }
}
