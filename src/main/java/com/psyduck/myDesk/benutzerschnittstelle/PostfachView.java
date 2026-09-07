package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.router.Route;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerSession;
import com.psyduck.myDesk.persistenz.Nachricht;
import com.psyduck.myDesk.persistenz.NachrichtService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
@StyleSheet("styles.css")
@Route(
	    value = "postfach",
	    layout = MainLayout.class
	)
public class PostfachView extends VerticalLayout {
	
	private final NachrichtService nachrichtService;

	public PostfachView(NachrichtService nachrichtService) {

	    this.nachrichtService = nachrichtService;

    	setSizeFull();
    	setPadding(true);
    	setSpacing(true);

        MasterDetailLayout layout = new MasterDetailLayout();
        layout.setExpandDetail(true);
        layout.setDetailSize("250px");
        layout.setExpandMaster(true);

        Grid<Nachricht> grid = new Grid<>(Nachricht.class, false);
        
        layout.setMaster(grid);
        layout.setDetail(null);

        grid.addColumn(nachricht -> nachricht.getAbsender().getName())
        		.setHeader("Absender")
        		.setFlexGrow(1);

        grid.addColumn(Nachricht::getTitel)
                .setHeader("Titel")
                .setFlexGrow(2);

        grid.addColumn(Nachricht::getVorschau)
                .setHeader("Vorschau")
                .setFlexGrow(3);

        grid.addComponentColumn(nachricht ->
	        formatiereDatumUndUhrzeit(nachricht.getEmpfangenAm()))
	        .setHeader("Empfangen am")
	        .setFlexGrow(2);

        grid.setSizeFull();  
        
        grid.setItems(
        		nachrichtService.getNachrichten(BenutzerSession.getAktuellerBenutzer())
        		);
        
        grid.addClassName("postfach-grid");

        VerticalLayout details = new VerticalLayout();
        details.setPadding(false);
        
        Button schliessenButton = new Button(VaadinIcon.CLOSE.create());
        schliessenButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        schliessenButton.getElement().setAttribute("aria-label", "Vorschau schließen");
        
        schliessenButton.addClickListener(event -> {
        	grid.asSingleSelect().clear();
        	layout.setDetail(null);
        });
        
        HorizontalLayout headerLayout = new HorizontalLayout(new H2("Details"), schliessenButton);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

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
        
        details.add(headerLayout, titel, von, nachricht);
        
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

        add(layout);
        expand(layout);

    }
	
	private HorizontalLayout formatiereDatumUndUhrzeit(LocalDateTime datum) {

	    LocalDate heute = LocalDate.now();

	    String tag;
	    String uhrzeit = datum.format(
	            DateTimeFormatter.ofPattern("HH:mm", Locale.GERMAN)
	    );

	    if (datum.toLocalDate().equals(heute)) {
	        tag = "heute";
	    } else if (datum.toLocalDate().equals(heute.minusDays(1))) {
	        tag = "gestern";
	    } else {
	        tag = datum.format(
	                DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN)
	        );
	    }

	    Span tagSpan = new Span(tag);
	    tagSpan.setWidth("90px");

	    Span uhrzeitSpan = new Span(uhrzeit);

	    HorizontalLayout datumLayout =
	            new HorizontalLayout(tagSpan, uhrzeitSpan);

	    datumLayout.setSpacing(false);
	    datumLayout.setPadding(false);
	    datumLayout.setAlignItems(FlexComponent.Alignment.CENTER);

	    return datumLayout;
	}
}
