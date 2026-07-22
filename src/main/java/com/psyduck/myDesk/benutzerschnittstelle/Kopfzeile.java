package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Kopfzeile extends HorizontalLayout {

	public Kopfzeile() { 
		setWidthFull(); 
		setJustifyContentMode(JustifyContentMode.END); 
		
		Button abbrechen = new Button("Abbrechen"); 
		Button abmelden = new Button("Abmelden"); 
		
		
		add(abbrechen, abmelden); 
	} 
	
	// Dashboard
	public Kopfzeile(String titel, boolean abmeldebutton) { 
		setWidthFull(); 
		setJustifyContentMode(JustifyContentMode.END); 
		
		Button abmelden = new Button("Abmelden"); 

		HorizontalLayout buttons = new HorizontalLayout(
                abmelden
        );

        buttons.setSpacing(true);
        
        add(buttons);
	} 
	
	// Postfach
	public Kopfzeile(String seitentitel, boolean aktualisieren, boolean neueNachricht, boolean abmeldebutton) { 
		setWidthFull(); 
		setJustifyContentMode(JustifyContentMode.END); 
		
        H2 titel = new H2(seitentitel);
        
		Button aktualisierenButton = new Button("Aktualisieren");
		Button neueAktualisierenButton = new Button("Neue Nachricht");
		Button abmelden = new Button("Abmelden"); 

		HorizontalLayout buttons = new HorizontalLayout(
				aktualisierenButton,
				neueAktualisierenButton,
	            abmelden
	    );

	    buttons.setSpacing(true);        
	    add(titel, buttons);
	    
	    expand(titel);
	} 
	
	// Neue Nachricht
    public Kopfzeile(boolean abbrechen, boolean abmelden) {

        setWidthFull();
        setAlignItems(Alignment.END);

        Button abbrechenButton = new Button("Abbrechen");
        Button abmeldenButton = new Button("Abmelden");

        HorizontalLayout buttons = new HorizontalLayout(
                abbrechenButton,
                abmeldenButton
        );

        buttons.setSpacing(true);
        
        add(buttons);
        
        
    }
}
