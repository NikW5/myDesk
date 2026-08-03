package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("chat")
public class ChatView extends VerticalLayout {

	public ChatView() {
		setSizeFull();
        setPadding(true);
        setSpacing(true);
        
		Kopfzeile kopfzeile = new Kopfzeile(Kopfzeile.Typ.CHAT);
		
		DummyText dummyText = new DummyText("Chat");
        add(kopfzeile, dummyText);
        expand(dummyText);
    }
}
