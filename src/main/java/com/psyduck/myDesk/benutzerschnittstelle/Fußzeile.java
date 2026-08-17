package com.psyduck.myDesk.benutzerschnittstelle;

import java.time.Year;

import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;

public class Fußzeile extends Footer {
	
	public Fußzeile() {
		int aktuellesJahr = Year.now().getValue();
		
        Span copyright = new Span("© " + aktuellesJahr);

        setWidthFull();
        getStyle()
        .set("bottom", "0")
        .set("z-index", "1000")
        .set("background", "white")
        .set("display", "flex")
        .set("justify-content", "center");

        add(copyright);
    }

}
