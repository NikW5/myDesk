package com.psyduck.myDesk.benutzerschnittstelle;
import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("todo")
public class ToDoView extends VerticalLayout {

	public ToDoView() {
		setSizeFull();
        setPadding(false);
        setSpacing(false);
        
		Kopfzeile kopfzeile = new Kopfzeile(Kopfzeilentyp.TODO);
		Fußzeile fußzeile = new Fußzeile();
		
		DummyText dummyText = new DummyText("To-Do");
        add(kopfzeile, dummyText, fußzeile);
        expand(dummyText);
	}

}
