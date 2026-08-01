package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.Year;

import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;

public class Fußzeile extends Footer {
	
	public Fußzeile() {
		int aktuellesJahr = Year.now().getValue();
		
        Span copyright = new Span("© " + aktuellesJahr);

        setWidthFull();
        getStyle().set("display", "flex");
        getStyle().set("justify-content", "center");
        getStyle().set("padding", "1rem");

        add(copyright);
    }

}
