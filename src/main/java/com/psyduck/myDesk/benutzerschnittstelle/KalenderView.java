package com.psyduck.myDesk.benutzerschnittstelle;
import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("kalender")
public class KalenderView extends VerticalLayout {

	public KalenderView() {
		setSizeFull();
        setPadding(true);
        setSpacing(true);
        
		Kopfzeile kopfzeile = new Kopfzeile(Kopfzeilentyp.KALENDER);
		Fußzeile fußzeile = new Fußzeile();

		DummyText dummyText = new DummyText("Kalender");
        add(kopfzeile, dummyText, fußzeile);
        expand(dummyText);

		
	}

}
