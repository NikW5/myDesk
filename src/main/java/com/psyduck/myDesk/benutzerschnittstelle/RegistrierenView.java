package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("registrieren")
@PageTitle("Registrieren")
public class RegistrierenView extends VerticalLayout {

private final TextField benutzernameFeld = new TextField("Benutzername");
private final EmailField emailFeld = new EmailField("E-Mail");
private final PasswordField passwortFeld = new PasswordField("Passwort");
private final PasswordField passwortBestaetigenFeld =
        new PasswordField("Passwort bestätigen");

private final Button registrierenButton = new Button("Registrieren");
private final Button zurueckButton = new Button("Zurück");

private final BenutzerService benutzerService;

public RegistrierenView(BenutzerService benutzerService) {

    this.benutzerService = benutzerService;

    ansichtAufbauen();

    registrierenButton.addClickListener(event -> registrieren());

    zurueckButton.addClickListener(
            event -> UI.getCurrent().navigate(LoginView.class)
    );
}

private void registrieren() {

    String benutzername = benutzernameFeld.getValue().trim();
    String email = emailFeld.getValue().trim();
    String passwort = passwortFeld.getValue();
    String passwortBestaetigt = passwortBestaetigenFeld.getValue();

    if (benutzername.isEmpty()
            || email.isEmpty()
            || passwort.isEmpty()
            || passwortBestaetigt.isEmpty()) {

        getUI().ifPresent(ui ->
                ui.getPage().executeJs(
                        "alert('Bitte füllen Sie alle Felder aus.')"
                )
        );

        return;
    }

    if (!passwort.equals(passwortBestaetigt)) {

        passwortBestaetigenFeld.setInvalid(true);
        passwortBestaetigenFeld.setErrorMessage(
                "Die Passwörter stimmen nicht überein."
        );

        return;
    }

    passwortBestaetigenFeld.setInvalid(false);

    if (benutzerService.findeNachEmail(email).isPresent()) {

        emailFeld.setInvalid(true);
        emailFeld.setErrorMessage(
                "Diese E-Mail-Adresse ist bereits registriert."
        );

        return;
    }

    emailFeld.setInvalid(false);

    Benutzer benutzer = new Benutzer(
            email,
            passwort,
            benutzername
    );

    benutzerService.speichern(benutzer);

    getUI().ifPresent(ui ->
            ui.getPage().executeJs(
                    "alert('Registrierung erfolgreich!')"
            )
    );

    UI.getCurrent().navigate(LoginView.class);
}

private void ansichtAufbauen() {

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    VerticalLayout layout = new VerticalLayout();
    layout.setWidth("700px");

    layout.getStyle()
            .set("border", "1px solid lightgray")
            .set("padding", "20px");

    VerticalLayout formularContainer = new VerticalLayout();
    formularContainer.setWidth("400px");
    formularContainer.setPadding(true);
    formularContainer.setSpacing(true);
    formularContainer.setAlignItems(Alignment.CENTER);

    formularContainer.getStyle()
            .set("border", "1px solid lightgray")
            .set("border-radius", "4px")
            .set("padding", "20px")
            .set("background-color", "#f8f8f8");

    H2 ueberschrift = new H2("Registrieren");

    FormLayout formular = new FormLayout();
    formular.setWidthFull();

    formular.add(
            benutzernameFeld,
            emailFeld,
            passwortFeld,
            passwortBestaetigenFeld
    );

    formular.setColspan(benutzernameFeld, 2);
    formular.setColspan(emailFeld, 2);
    formular.setColspan(passwortFeld, 2);
    formular.setColspan(passwortBestaetigenFeld, 2);

    HorizontalLayout buttonLeiste = new HorizontalLayout(
            registrierenButton,
            zurueckButton
    );

    formularContainer.add(
            ueberschrift,
            formular,
            buttonLeiste
    );

    layout.add(formularContainer);

    add(layout);
}


}