package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("chat")
public class ChatView extends VerticalLayout {

	public ChatView() {
		setSizeFull();
		setPadding(false);
		setSpacing(false);
        
		Kopfzeile kopfzeile = new Kopfzeile(Kopfzeilentyp.CHAT);
		Fußzeile fußzeile = new Fußzeile();
		
		DummyText dummyText = new DummyText("Chat");
        add(kopfzeile, dummyText, fußzeile);
        expand(dummyText);
    }
}
