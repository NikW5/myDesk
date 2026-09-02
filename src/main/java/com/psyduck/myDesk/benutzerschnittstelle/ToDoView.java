package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(
	    value = "todo",
	    layout = MainLayout.class
	)
public class ToDoView extends VerticalLayout {

	public ToDoView() {
		setSizeFull();
        setPadding(true);
        setSpacing(true);
		
		DummyText dummyText = new DummyText("To-Do");
        add(dummyText);
        expand(dummyText);
	}

}
