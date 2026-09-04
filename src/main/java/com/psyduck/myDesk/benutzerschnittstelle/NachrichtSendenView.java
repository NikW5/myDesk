package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Anhang;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.psyduck.myDesk.persistenz.BenutzerSession;
import com.psyduck.myDesk.persistenz.Nachricht;
import com.psyduck.myDesk.persistenz.NachrichtService;
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

@Route(
	    value = "neue_nachricht",
	    layout = MainLayout.class
	)
@PageTitle("Neue Nachricht")
public class NachrichtSendenView extends VerticalLayout {

	private final List<Anhang> anhaenge = new ArrayList<>();
	private final VerticalLayout anhangListe = new VerticalLayout();
	private final ComboBox<Benutzer> empfaenger = new ComboBox<>("An");
	private final TextField betreff = new TextField("Titel");
	private final TextArea nachricht = new TextArea("Neue Nachricht");

	private final BenutzerService benutzerService;
	private final NachrichtService nachrichtService;

	public NachrichtSendenView(BenutzerService benutzerService, NachrichtService nachrichtService) {

	    this.benutzerService = benutzerService;
	    this.nachrichtService = nachrichtService;

	    setSizeFull();
	    setAlignItems(Alignment.CENTER);
	    setPadding(true);
	    setSpacing(true);

	    Component registrierungsbereich = erstelleRegestrierungsbereich();

	    add(registrierungsbereich);
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
         empfaenger.setWidthFull();
         betreff.setWidthFull();

         formular.add(empfaenger, betreff);
         formular.setResponsiveSteps(
                 new FormLayout.ResponsiveStep("0", 1)
         );

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

        senden.addClickListener(event -> {
        	Benutzer absender = BenutzerSession.getAktuellerBenutzer();
        	Benutzer empfaengerBenutzer = empfaenger.getValue();

        	if (absender == null) {
        	    getUI().ifPresent(ui ->
        	        ui.getPage().executeJs(
        	            "alert('Kein Benutzer ist eingeloggt.')"
        	        )
        	    );
        	    return;
        	}


            if (empfaengerBenutzer == null) {
                empfaenger.setInvalid(true);
                empfaenger.setErrorMessage(
                        "Bitte wählen Sie einen Empfänger aus."
                );
                return;
            }

            if (betreff.getValue().trim().isEmpty()) {
                betreff.setInvalid(true);
                betreff.setErrorMessage(
                        "Bitte geben Sie einen Titel ein."
                );
                return;
            }

            if (nachricht.getValue().trim().isEmpty()) {
                nachricht.setInvalid(true);
                nachricht.setErrorMessage(
                        "Bitte geben Sie eine Nachricht ein."
                );
                return;
            }

            betreff.setInvalid(false);
            nachricht.setInvalid(false);

            nachrichtService.speichern(
                    absender,
                    empfaengerBenutzer,
                    betreff.getValue().trim(),
                    nachricht.getValue()
            );

            betreff.clear();
            nachricht.clear();
            empfaenger.clear();

        });

        layout.add(senden);

        return layout;
    }

}