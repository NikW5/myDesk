package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@Route(
	    value = "chat",
	    layout = MainLayout.class
	)
@PermitAll
public class ChatView extends VerticalLayout {

	public ChatView() {
		setSizeFull();
		setPadding(true);
		setSpacing(true);
		
		DummyText dummyText = new DummyText("Chat");
        add(dummyText);
        expand(dummyText);
    }
}
