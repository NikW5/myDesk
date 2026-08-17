package com.psyduck.myDesk.benutzerschnittstelle;
import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("todo")
public class ToDoView extends VerticalLayout {

	public ToDoView() {
		setSizeFull();
        setPadding(true);
        setSpacing(true);
        
		Kopfzeile kopfzeile = new Kopfzeile(Kopfzeilentyp.TODO);
		Fußzeile fußzeile = new Fußzeile();
		
		DummyText dummyText = new DummyText("To-Do");
        add(kopfzeile, fußzeile, dummyText);
        expand(dummyText);
	}

}
