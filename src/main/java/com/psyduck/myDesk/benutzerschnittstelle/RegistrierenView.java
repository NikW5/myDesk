
package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

    private final BenutzerService benutzerService;
    private final MessageSource messageSource;

    private final TextField benutzernameFeld =
            new TextField();

    private final EmailField emailFeld =
            new EmailField();

    private final TextField vornameFeld =
            new TextField();

    private final TextField nachnameFeld =
            new TextField();

    private final PasswordField passwortFeld =
            new PasswordField();

    private final PasswordField passwortBestaetigenFeld =
            new PasswordField();

    private final Button registrierenButton =
            new Button();

    private final Button zurueckButton =
            new Button();

    public RegistrierenView(
            BenutzerService benutzerService,
            MessageSource messageSource) {

        this.benutzerService = benutzerService;
        this.messageSource = messageSource;

        initialisiereLayout();
        initialisiereFelder();
        initialisiereButtons();
        ansichtAufbauen();
    }

    private void initialisiereLayout() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        addClassName("registrieren-background");
    }

    private void initialisiereFelder() {

        benutzernameFeld.setLabel(
                getText("registrieren.username")
        );

        emailFeld.setLabel(
                getText("registrieren.email")
        );

        vornameFeld.setLabel(
                getText("registrieren.firstname")
        );

        nachnameFeld.setLabel(
                getText("registrieren.lastname")
        );

        passwortFeld.setLabel(
                getText("registrieren.password")
        );

        passwortBestaetigenFeld.setLabel(
                getText("registrieren.password.confirm")
        );

        benutzernameFeld.setRequired(true);
        emailFeld.setRequired(true);
        passwortFeld.setRequired(true);
        passwortBestaetigenFeld.setRequired(true);

        emailFeld.setErrorMessage(
                getText("registrieren.email.invalid")
        );

        benutzernameFeld.setWidthFull();
        emailFeld.setWidthFull();
        vornameFeld.setWidthFull();
        nachnameFeld.setWidthFull();
        passwortFeld.setWidthFull();
        passwortBestaetigenFeld.setWidthFull();

        benutzernameFeld.addClassName("registrieren-field");
        emailFeld.addClassName("registrieren-field");
        vornameFeld.addClassName("registrieren-field");
        nachnameFeld.addClassName("registrieren-field");
        passwortFeld.addClassName("registrieren-field");
        passwortBestaetigenFeld.addClassName("registrieren-field");
    }

    private void initialisiereButtons() {

        registrierenButton.setText(
                getText("registrieren.button")
        );

        registrierenButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        registrierenButton.addClassName(
                "registrieren-submit-button"
        );

        registrierenButton.addClickListener(
                event -> registrieren()
        );

        zurueckButton.setText(
                getText("registrieren.back")
        );

        zurueckButton.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY
        );

        zurueckButton.addClassName(
                "registrieren-back-button"
        );

        zurueckButton.addClickListener(
                event -> UI.getCurrent().navigate(LoginView.class)
        );
    }

    private void registrieren() {

        String benutzername = benutzernameFeld.getValue().trim();
        String email = emailFeld.getValue().trim();
        String vorname = vornameFeld.getValue().trim();
        String nachname = nachnameFeld.getValue().trim();

        String passwort = passwortFeld.getValue();
        String bestaetigung = passwortBestaetigenFeld.getValue();

        if (benutzername.isEmpty()
                || email.isEmpty()
                || passwort.isEmpty()
                || bestaetigung.isEmpty()) {

            Notification.show(
                    getText("registrieren.required")
            );

            return;
        }

        if (emailFeld.isInvalid()) {

            emailFeld.setInvalid(true);

            emailFeld.setErrorMessage(
                    getText("registrieren.email.invalid")
            );

            return;
        }

        if (!passwort.equals(bestaetigung)) {

            passwortBestaetigenFeld.setInvalid(true);

            passwortBestaetigenFeld.setErrorMessage(
                    getText("registrieren.password.mismatch")
            );

            return;
        }

        passwortBestaetigenFeld.setInvalid(false);

        if (!pruefeBenutzername(benutzername)) {
            return;
        }

        if (!pruefeEmail(email)) {
            return;
        }

        speichereBenutzer(
                benutzername,
                email,
                vorname,
                nachname,
                passwort
        );
    }

    private boolean pruefeBenutzername(String benutzername) {

        if (benutzerService.findeNachBenutzername(benutzername).isPresent()) {

            benutzernameFeld.setInvalid(true);

            benutzernameFeld.setErrorMessage(
                    getText("registrieren.username.exists")
            );

            return false;
        }

        benutzernameFeld.setInvalid(false);

        return true;
    }

    private boolean pruefeEmail(String email) {

        if (benutzerService.findeNachEmail(email).isPresent()) {

            emailFeld.setInvalid(true);

            emailFeld.setErrorMessage(
                    getText("registrieren.email.exists")
            );

            return false;
        }

        emailFeld.setInvalid(false);

        return true;
    }

    private void speichereBenutzer(
            String benutzername,
            String email,
            String vorname,
            String nachname,
            String passwort) {

        Benutzer benutzer = new Benutzer(
                email,
                passwort,
                benutzername
        );

        benutzer.setVorname(
                vorname.isBlank() ? null : vorname
        );

        benutzer.setNachname(
                nachname.isBlank() ? null : nachname
        );

        benutzerService.speichern(benutzer);

        Notification.show(
                getText("registrieren.success")
        );

        UI.getCurrent().navigate(LoginView.class);
    }

    private void ansichtAufbauen() {

        H2 titel = new H2(
                getText("registrieren.title")
        );

        titel.addClassName(
                "registrieren-title"
        );

        FormLayout formular = new FormLayout();

        formular.setWidthFull();

        formular.add(
                benutzernameFeld,
                emailFeld,
                vornameFeld,
                nachnameFeld,
                passwortFeld,
                passwortBestaetigenFeld
        );

        HorizontalLayout buttonLeiste =
                new HorizontalLayout(
                        zurueckButton,
                        registrierenButton
                );

        buttonLeiste.setWidthFull();

        buttonLeiste.setJustifyContentMode(
                JustifyContentMode.END
        );

        buttonLeiste.addClassName(
                "registrieren-button-bar"
        );

        VerticalLayout container =
                new VerticalLayout(
                        titel,
                        formular,
                        buttonLeiste
                );

        container.setMaxWidth("442px");
        container.setWidthFull();
        container.setPadding(true);
        container.setSpacing(true);

        container.addClassName(
                "registrieren-container"
        );

        add(container);
    }

    private String getText(String schluessel) {

        return messageSource.getMessage(
                schluessel,
                null,
                Locale.GERMAN
        );
    }
}
