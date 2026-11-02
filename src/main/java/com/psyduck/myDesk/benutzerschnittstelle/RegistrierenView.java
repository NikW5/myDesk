package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("Registrieren")
@PageTitle("Registrieren")
@AnonymousAllowed
public class RegistrierenView extends VerticalLayout {
 
	private final TextField benutzernameFeld = new TextField("Benutzername");
    private final EmailField emailFeld = new EmailField("E-Mail");
    private final TextField vornameFeld = new TextField("Vorname (optional)");
    private final TextField nachnameFeld = new TextField("Nachname (optional)");
    private final PasswordField passwortFeld = new PasswordField("Passwort");
    private final PasswordField passwortBestaetigenFeld = new PasswordField("Passwort bestätigen");
    private final Button registrierenButton = new Button("Registrieren");
    private final Button zurueckButton = new Button("Zurück");
    private final BenutzerService benutzerService;

    public RegistrierenView(BenutzerService benutzerService) {
    	
        this.benutzerService = benutzerService;
        
        ansichtAufbauen();
        
        registrierenButton.addClickListener(event -> registrieren());
        zurueckButton.addClickListener(event -> UI.getCurrent().navigate(LoginView.class));
    }

    private void registrieren() {
        String benutzername = benutzernameFeld.getValue().trim();
        String email = emailFeld.getValue().trim();
        String vorname = vornameFeld.getValue().trim();
        String nachname = nachnameFeld.getValue().trim();
        String passwort = passwortFeld.getValue();
        String bestaetigung = passwortBestaetigenFeld.getValue();

        if (benutzername.isEmpty() || email.isEmpty() || passwort.isEmpty() || bestaetigung.isEmpty()) {
            Notification.show("Bitte füllen Sie alle Pflichtfelder aus.");
            return;
        }

        if (emailFeld.isInvalid()) {
            emailFeld.setErrorMessage("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
            return;
        }

        if (!passwort.equals(bestaetigung)) {
            passwortBestaetigenFeld.setInvalid(true);
            passwortBestaetigenFeld.setErrorMessage("Die Passwörter stimmen nicht überein.");
            return;
        }

        passwortBestaetigenFeld.setInvalid(false);

        if (benutzerService.findeNachBenutzername(benutzername).isPresent()) {
            benutzernameFeld.setInvalid(true);
            benutzernameFeld.setErrorMessage("Dieser Benutzername ist bereits vergeben.");
            return;
        }

        benutzernameFeld.setInvalid(false);

        if (benutzerService.findeNachEmail(email).isPresent()) {
            emailFeld.setInvalid(true);
            emailFeld.setErrorMessage("Diese E-Mail-Adresse ist bereits registriert.");
            return;
        }

        emailFeld.setInvalid(false);

        Benutzer benutzer = new Benutzer(email, passwort, benutzername);
        benutzer.setVorname(vorname.isBlank() ? null : vorname);
        benutzer.setNachname(nachname.isBlank() ? null : nachname);
        benutzerService.speichern(benutzer);

        Notification.show("Registrierung erfolgreich!");
        UI.getCurrent().navigate(LoginView.class);
    }

    private void ansichtAufbauen() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout container = new VerticalLayout();
        container.setMaxWidth("442px");
        container.setWidthFull();
        container.setPadding(true);
        container.setSpacing(true);
        container.setAlignItems(Alignment.CENTER);
        container.getStyle().set("border", "1px solid lightgray").set("border-radius", "4px").set("background-color", "#f8f8f8");

        FormLayout formular = new FormLayout();
        formular.setWidthFull();
        formular.add(benutzernameFeld, emailFeld, vornameFeld, nachnameFeld, passwortFeld, passwortBestaetigenFeld);

        HorizontalLayout buttonLeiste = new HorizontalLayout(registrierenButton, zurueckButton);
        container.add(new H2("Registrieren"), formular, buttonLeiste);
        add(container);
    }

}