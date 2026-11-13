package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

@Route(value = "profil", layout = MainLayout.class)
@PageTitle("Mein Profil")
@PermitAll
public class ProfilView extends VerticalLayout {

    private final AktuellerBenutzerService aktuellerBenutzerService;
    private final BenutzerService benutzerService;

    private final TextField benutzernameFeld = new TextField("Benutzername");
    private final EmailField emailFeld = new EmailField("E-Mail");
    private final TextField vornameFeld = new TextField("Vorname (optional)");
    private final TextField nachnameFeld = new TextField("Nachname (optional)");

    private final PasswordField aktuellesPasswortFeld = new PasswordField("Aktuelles Passwort");
    private final PasswordField neuesPasswortFeld = new PasswordField("Neues Passwort");
    private final PasswordField passwortBestaetigenFeld = new PasswordField("Neues Passwort bestätigen");

    public ProfilView(AktuellerBenutzerService aktuellerBenutzerService, BenutzerService benutzerService) {
        this.aktuellerBenutzerService = aktuellerBenutzerService;
        this.benutzerService = benutzerService;

        setWidthFull();
        setMaxWidth("800px");
        setPadding(true);
        setSpacing(true);

        Benutzer benutzer = aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            return;
        }

        add(erstelleProfilBereich(benutzer), erstellePasswortBereich(benutzer), erstelleKontoloeschBereich(benutzer));
    }

    private VerticalLayout erstelleProfilBereich(Benutzer benutzer) {
        FormLayout formular = new FormLayout();
        formular.add(benutzernameFeld, emailFeld, vornameFeld, nachnameFeld);

        benutzernameFeld.setValue(benutzer.getName());
        emailFeld.setValue(benutzer.getEmail());
        vornameFeld.setValue(benutzer.getVorname() == null ? "" : benutzer.getVorname());
        nachnameFeld.setValue(benutzer.getNachname() == null ? "" : benutzer.getNachname());

        Button speichernButton = new Button("Änderungen speichern", event -> {
            if (benutzernameFeld.isEmpty() || emailFeld.isEmpty() || emailFeld.isInvalid()) {
                Notification.show("Bitte prüfen Sie Benutzername und E-Mail-Adresse.");
                return;
            }

            try {
                benutzerService.aktualisiereProfil(benutzer, benutzernameFeld.getValue(), emailFeld.getValue(), vornameFeld.getValue(), nachnameFeld.getValue());
                Notification.show("Profil erfolgreich aktualisiert.");
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage());
            }
        });

        VerticalLayout bereich = new VerticalLayout(new H2("Mein Profil"), formular, speichernButton);
        bereich.setPadding(true);
        bereich.setSpacing(true);
        bereich.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)").set("border-radius", "var(--lumo-border-radius-m)");
        return bereich;
    }

    private VerticalLayout erstellePasswortBereich(Benutzer benutzer) {
        FormLayout formular = new FormLayout();
        formular.add(neuesPasswortFeld, aktuellesPasswortFeld, passwortBestaetigenFeld);
        
        Button aendernButton = new Button("Passwort ändern", event -> {
            String aktuell = aktuellesPasswortFeld.getValue();
            String neu = neuesPasswortFeld.getValue();
            String bestaetigung = passwortBestaetigenFeld.getValue();

            if (aktuell.isBlank() || neu.isBlank() || bestaetigung.isBlank()) {
                Notification.show("Bitte füllen Sie alle Passwortfelder aus.");
                return;
            }

            if (!neu.equals(bestaetigung)) {
                passwortBestaetigenFeld.setInvalid(true);
                passwortBestaetigenFeld.setErrorMessage("Die Passwörter stimmen nicht überein.");
                return;
            }

            passwortBestaetigenFeld.setInvalid(false);

            try {
                benutzerService.aenderePasswort(benutzer, aktuell, neu);
                aktuellesPasswortFeld.clear();
                neuesPasswortFeld.clear();
                passwortBestaetigenFeld.clear();
                Notification.show("Passwort erfolgreich geändert.");
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage());
            }
        });

        VerticalLayout bereich = new VerticalLayout(new H2("Passwort ändern"), formular, aendernButton);
        bereich.setPadding(true);
        bereich.setSpacing(true);
        bereich.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)").set("border-radius", "var(--lumo-border-radius-m)");
        return bereich;
    }


	private VerticalLayout erstelleKontoloeschBereich(Benutzer benutzer) {
	    Button loeschenButton = new Button("Konto löschen", event -> zeigeLoeschdialog(benutzer));
	    loeschenButton.setThemeName("error");
	
	    VerticalLayout bereich = new VerticalLayout(
	            new H2("Konto löschen"),
	            new Paragraph(
	                    "Dein Konto und deine To-dos werden dauerhaft gelöscht. " +
	                    "Nachrichten und Anhänge bleiben erhalten."
	            ),
	            loeschenButton
	    );
	
	    bereich.setPadding(true);
	    bereich.setSpacing(true);
	    bereich.setWidthFull();
	
	    bereich.getStyle()
	            .set("border", "1px solid var(--lumo-error-color-50pct)")
	            .set("border-radius", "var(--lumo-border-radius-l)")
	            .set("background-color", "var(--lumo-error-color-10pct)");
	
	    return bereich;
	}



	private void zeigeLoeschdialog(Benutzer benutzer) {
	    Dialog dialog = new Dialog();
	    dialog.setHeaderTitle("Konto dauerhaft löschen");
	    dialog.setWidth("450px");
	    dialog.setMaxWidth("95vw");
	    dialog.setCloseOnOutsideClick(false);
	
	    PasswordField passwortFeld = new PasswordField("Aktuelles Passwort");
	    passwortFeld.setWidthFull();
	
	    Checkbox bestaetigung = new Checkbox(
	            "Ich möchte mein Konto endgültig löschen."
	    );
	
	    Button abbrechenButton = new Button("Abbrechen", event -> dialog.close());
	
	    Button endgueltigLoeschenButton = new Button(
	            "Endgültig löschen",
	            event -> {
	                if (passwortFeld.isEmpty()) {
	                    passwortFeld.setInvalid(true);
	                    passwortFeld.setErrorMessage(
	                            "Bitte geben Sie Ihr aktuelles Passwort ein."
	                    );
	                    return;
	                }
	
	                try {
	                    benutzerService.loescheKonto(
	                            benutzer, passwortFeld.getValue()
	                    );
	
	                    dialog.close();
	                    aktuellerBenutzerService.abmelden();
	
	                    getUI().ifPresent(ui -> ui.navigate(LoginView.class));
	
	                    Notification.show("Dein Konto wurde gelöscht.");
	
	                } catch (IllegalArgumentException e) {
	                    passwortFeld.setInvalid(true);
	                    passwortFeld.setErrorMessage(e.getMessage());
	                }
	            }
	    );
	
	    endgueltigLoeschenButton.setEnabled(false);
	    endgueltigLoeschenButton.addThemeVariants(
	            com.vaadin.flow.component.button.ButtonVariant.LUMO_ERROR
	    );
	
	    bestaetigung.addValueChangeListener(event ->
	            endgueltigLoeschenButton.setEnabled(
	                    Boolean.TRUE.equals(event.getValue())
	            )
	    );
	
	    HorizontalLayout aktionen = new HorizontalLayout(
	            abbrechenButton, endgueltigLoeschenButton
	    );
	
	    aktionen.setWidthFull();
	    aktionen.setJustifyContentMode(
	            FlexComponent.JustifyContentMode.END
	    );
	
	    VerticalLayout inhalt = new VerticalLayout(
	            new Paragraph(
	                    "Diese Aktion kann nicht rückgängig gemacht werden."
	            ),
	            passwortFeld,
	            bestaetigung,
	            aktionen
	    );
	
	    inhalt.setPadding(false);
	    inhalt.setSpacing(true);
	
	    dialog.add(inhalt);
	    dialog.open();
	}

}