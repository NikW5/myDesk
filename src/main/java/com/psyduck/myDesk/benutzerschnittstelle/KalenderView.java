package com.psyduck.myDesk.benutzerschnittstelle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendar.Option;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
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

	public KalenderView() {
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

	    return kalender;
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

	    add(kalenderHeader, kalender);

	    return kalender;
	}

}
