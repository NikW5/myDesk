package com.psyduck.myDesk.benutzerschnittstelle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("kalender")
public class KalenderView extends VerticalLayout {

	public KalenderView() {
		setSizeFull();
        setPadding(true);
        setSpacing(true);
        
		Kopfzeile kopfzeile = new Kopfzeile(Kopfzeile.Typ.KALENDER);
		
		add(kopfzeile);
	}

}
