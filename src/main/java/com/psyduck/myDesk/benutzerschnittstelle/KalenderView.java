package com.psyduck.myDesk.benutzerschnittstelle;
import com.psyduck.myDesk.benutzerschnittstelle.layout.Fußzeile;
import com.psyduck.myDesk.benutzerschnittstelle.layout.Kopfzeile;
import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
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

		DummyText dummyText = new DummyText("Kalender");
        add(dummyText);
        expand(dummyText);

		
	}

}
