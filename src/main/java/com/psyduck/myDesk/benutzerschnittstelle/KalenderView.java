package com.psyduck.myDesk.benutzerschnittstelle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendar.Option;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.ToDo;
import com.psyduck.myDesk.persistenz.ToDoRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(
	    value = "kalender",
	    layout = MainLayout.class
	)
public class KalenderView extends VerticalLayout {
	
	private final ToDoRepository toDoRepository;

	public KalenderView(ToDoRepository toDoRepository) {
		this.toDoRepository = toDoRepository;
		
		setSizeFull();
		setPadding(true);
		setSpacing(true);

		Component kalender = erstelleKalenderbereich();
        add(kalender);
        expand(kalender);	
	}
	
	private FullCalendar erstelleKalender() {

	    FullCalendar kalender = FullCalendarBuilder.create().build();

	    kalender.setSizeFull();

	    kalender.setOption(
	        Option.LOCALE,
	        Locale.GERMAN
	    );

	    kalender.setOption(
	        FullCalendar.Option.HEADER_TOOLBAR,
	        "prev,next today title"
	    );
	    
	    fuegeTodosZumKalenderHinzu(kalender);

	    return kalender;
	}
	
	private void fuegeTodosZumKalenderHinzu(FullCalendar kalender) {

	    for (ToDo aufgabe : toDoRepository.findAll()) {

	        if (aufgabe.getFaelligAm() == null) {
	            continue;
	        }

	        String titel = aufgabe.isErledigt()
	            ? "✓ " + aufgabe.getText()
	            : aufgabe.getText();

	        Entry eintrag = new Entry(
	            aufgabe.getId().toString()
	        );

	        eintrag.setTitle(titel);
	        eintrag.setStart(aufgabe.getFaelligAm());
	        eintrag.setAllDay(true);

	        if (aufgabe.isErledigt()) {
	            eintrag.setColor("#43A047");
	            eintrag.setTextColor("#FFFFFF");
	        } else {
	            eintrag.setColor("#1976D2");
	            eintrag.setTextColor("#FFFFFF");
	        }

	        kalender.getEntryProvider()
	            .asInMemory()
	            .addEntry(eintrag);
	    }
	}

	private HorizontalLayout erstelleKalenderheader(FullCalendar kalender) {

	    Button previous = new Button("‹", event -> kalender.previous());
	    Button today = new Button("Heute", event -> kalender.today());
	    Button next = new Button("›", event -> kalender.next());

	    HorizontalLayout navigation = new HorizontalLayout(
	        previous,
	        today,
	        next
	    );

	    navigation.setPadding(false);
	    navigation.setSpacing(true);

	    Span monatAnzeige = new Span();

	    monatAnzeige.getStyle()
	        .set("font-size", "24px")
	        .set("font-weight", "600")
	        .set("color", "var(--lumo-primary-text-color)")
	        .set("position", "absolute")
	        .set("left", "50%")
	        .set("transform", "translateX(-50%)");

	    HorizontalLayout kalenderHeader = new HorizontalLayout();

	    kalenderHeader.setWidthFull();
	    kalenderHeader.setPadding(false);
	    kalenderHeader.setAlignItems(
	        FlexComponent.Alignment.CENTER
	    );

	    kalenderHeader.getStyle()
	        .set("position", "relative");

	    kalenderHeader.add(navigation);
	    kalenderHeader.add(monatAnzeige);

	    kalender.addDatesRenderedListener(event -> {

	        LocalDate start = event.getIntervalStart();

	        monatAnzeige.setText(
	            start.format(
	                DateTimeFormatter.ofPattern(
	                    "MMMM yyyy",
	                    Locale.GERMAN
	                )
	            )
	        );
	    });

	    return kalenderHeader;
	}

	private Component erstelleKalenderbereich() {

	    FullCalendar kalender = erstelleKalender();

	    HorizontalLayout kalenderHeader = erstelleKalenderheader(kalender);

	    VerticalLayout kalenderbereich = new VerticalLayout(
	            kalenderHeader,
	            kalender
	        );

	        kalenderbereich.setSizeFull();
	        kalenderbereich.setPadding(false);
	        kalenderbereich.setSpacing(false);

	        kalenderbereich.expand(kalender);

	        return kalenderbereich;
	}

}
