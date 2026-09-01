package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.persistenz.Anhang;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import java.util.ArrayList;
import java.util.List;

@Route("neue_nachricht")
@PageTitle("Neue Nachricht")
public class NachrichtSendenView extends VerticalLayout {

	private final List<Anhang> anhaenge = new ArrayList<>();
	private final VerticalLayout anhangListe = new VerticalLayout();
	private final ComboBox<Benutzer> empfaenger = new ComboBox<>("An");
	private final BenutzerService benutzerService;

	public NachrichtSendenView(BenutzerService benutzerService) {
	    this.benutzerService = benutzerService;

	    setSizeFull();
	    setAlignItems(Alignment.CENTER);
	    setPadding(false);
	    setSpacing(false);

	    Kopfzeile kopfzeile = new Kopfzeile(Kopfzeilentyp.NACHRICHT_SENDEN);
	    Fußzeile fußzeile = new Fußzeile();

	    kopfzeile.getStyle().set("margin-bottom", "20px");

	    Component registrierungsbereich = erstelleRegestrierungsbereich();

	    add(
	        kopfzeile,
	        registrierungsbereich,
	        fußzeile
	    );

	    expand(registrierungsbereich);
	}


    private Component erstelleRegestrierungsbereich() {

    	VerticalLayout layout = new VerticalLayout();
    	layout.setWidth("700px");
    	layout.getStyle().set("border", "1px solid lightgray");
    	layout.getStyle().set("padding", "20px");

    	layout.add(
                erstelleNachrichtenbereich(),
                erstelleAnhangbereich(),
                erstelleSendenbutton()
        );

        return layout;

    }

    
    private Component erstelleNachrichtenbereich() {
    	
    	 VerticalLayout layout = new VerticalLayout();
         layout.setPadding(false);

         FormLayout formular = new FormLayout();

         empfaenger.setItems(benutzerService.getBenutzer());
         empfaenger.setItemLabelGenerator(Benutzer::getName);

         TextField betreff = new TextField("Titel");

         empfaenger.setWidthFull();
         betreff.setWidthFull();

         formular.add(empfaenger, betreff);
         formular.setResponsiveSteps(
                 new FormLayout.ResponsiveStep("0", 1)
         );

         TextArea nachricht = new TextArea("Neue Nachricht");
         nachricht.setWidthFull();
         nachricht.setHeight("250px");

         layout.add(
                 formular,
                 nachricht
         );

         return layout;
    }
    
    private Component erstelleAnhangbereich() {
    	
    	 VerticalLayout layout = new VerticalLayout();
         layout.setPadding(false);
         layout.setSpacing(true);

         Span anhangLabel = new Span("Anhang:");

         Upload upload = new Upload(
        		    UploadHandler.inMemory((metadata, bytes) -> {

        		        Anhang anhang = new Anhang(
        		                metadata.fileName(),
        		                bytes
        		        );

        		        anhaenge.add(anhang);
        		        anhangListe.add(erstelleAnhang(anhang));
        		    })
        		);

        		upload.setMaxFiles(20);

        layout.add(anhangLabel, upload, anhangListe);

        return layout;
    }
    
    
    
    private Component erstelleAnhang(Anhang anhang) {

    	HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setAlignItems(Alignment.CENTER);

        Span dateiname = new Span(anhang.getDateiname());

        Button loeschen = new Button("X");

        loeschen.addClickListener(event -> {
            anhaenge.remove(anhang);
            anhangListe.remove(layout);
        });

        layout.expand(dateiname);
        layout.add(dateiname, loeschen);

        return layout;
    }
    
    private Component erstelleSendenbutton() { 
    	
    	HorizontalLayout layout = new HorizontalLayout(); 
    	layout.setWidthFull(); 
    	layout.setJustifyContentMode(JustifyContentMode.END); 
    	
    	Button senden = new Button("Senden"); 
    	layout.add(senden); 
    	
    	return layout; 
   	}
}