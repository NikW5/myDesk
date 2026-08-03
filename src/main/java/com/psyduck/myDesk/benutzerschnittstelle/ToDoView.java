package com.psyduck.myDesk.benutzerschnittstelle;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("todo")
public class ToDoView extends VerticalLayout {

	public ToDoView() {
		setSizeFull();
        setPadding(true);
        setSpacing(true);
        
		Kopfzeile kopfzeile = new Kopfzeile(Kopfzeile.Typ.TODO);
		
		add(kopfzeile);
	}

}
