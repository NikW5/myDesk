
package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@Route(value = "profil", layout = MainLayout.class)
@PageTitle("Mein Profil")
@PermitAll
public class ProfilView extends VerticalLayout {

    private final AktuellerBenutzerService aktuellerBenutzerService;
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

    private final PasswordField aktuellesPasswortFeld =
            new PasswordField();

    private final PasswordField neuesPasswortFeld =
            new PasswordField();

    private final PasswordField passwortBestaetigenFeld =
            new PasswordField();

    public ProfilView(
            AktuellerBenutzerService aktuellerBenutzerService,
            BenutzerService benutzerService,
            MessageSource messageSource) {

        this.aktuellerBenutzerService = aktuellerBenutzerService;
        this.benutzerService = benutzerService;
        this.messageSource = messageSource;

        initialisiereLayout();

        Benutzer benutzer =
                aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) {
            getUI().ifPresent(ui ->
                    ui.navigate(LoginView.class)
            );
            return;
        }

        add(
                erstelleProfilBereich(benutzer),
                erstellePasswortBereich(benutzer),
                erstelleKontoloeschBereich(benutzer)
        );
    }

    private void initialisiereLayout() {

        setWidthFull();
        setMaxWidth("800px");
        setPadding(true);
        setSpacing(true);

        addClassName("profil-background");
    }

    private VerticalLayout erstelleProfilBereich(
            Benutzer benutzer) {

        benutzernameFeld.setLabel(
                getText("profil.username")
        );

        emailFeld.setLabel(
                getText("profil.email")
        );

        vornameFeld.setLabel(
                getText("profil.firstname")
        );

        nachnameFeld.setLabel(
                getText("profil.lastname")
        );

        benutzernameFeld.setValue(benutzer.getName());
        emailFeld.setValue(benutzer.getEmail());

        vornameFeld.setValue(
                benutzer.getVorname() == null
                        ? ""
                        : benutzer.getVorname()
        );

        nachnameFeld.setValue(
                benutzer.getNachname() == null
                        ? ""
                        : benutzer.getNachname()
        );

        FormLayout formular = new FormLayout();

        formular.add(
                benutzernameFeld,
                emailFeld,
                vornameFeld,
                nachnameFeld
        );

        Button speichernButton = new Button(
                getText("profil.save"),
                event -> speichereProfil(benutzer)
        );

        speichernButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        VerticalLayout bereich = new VerticalLayout(
                new H2(getText("profil.section.title")),
                formular,
                speichernButton
        );

        bereich.setPadding(true);
        bereich.setSpacing(true);
        bereich.setWidthFull();
        bereich.addClassName("profil-section");

        return bereich;
    }

    private void speichereProfil(Benutzer benutzer) {

        if (benutzernameFeld.isEmpty()
                || emailFeld.isEmpty()
                || emailFeld.isInvalid()) {

            Notification.show(
                    getText("profil.save.error")
            );

            return;
        }

        try {

            benutzerService.aktualisiereProfil(
                    benutzer,
                    benutzernameFeld.getValue().trim(),
                    emailFeld.getValue().trim(),
                    vornameFeld.getValue().trim(),
                    nachnameFeld.getValue().trim()
            );

            Notification.show(
                    getText("profil.save.success")
            );

        } catch (IllegalArgumentException e) {

            Notification.show(e.getMessage());
        }
    }

    private VerticalLayout erstellePasswortBereich(
            Benutzer benutzer) {

        aktuellesPasswortFeld.setLabel(
                getText("profil.password.current")
        );

        neuesPasswortFeld.setLabel(
                getText("profil.password.new")
        );

        passwortBestaetigenFeld.setLabel(
                getText("profil.password.confirm")
        );

        FormLayout formular = new FormLayout();

        formular.add(
                aktuellesPasswortFeld,
                neuesPasswortFeld,
                passwortBestaetigenFeld
        );

        Button aendernButton = new Button(
                getText("profil.password.change"),
                event -> aenderePasswort(benutzer)
        );

        aendernButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        VerticalLayout bereich = new VerticalLayout(
                new H2(getText("profil.password.title")),
                formular,
                aendernButton
        );

        bereich.setPadding(true);
        bereich.setSpacing(true);
        bereich.setWidthFull();
        bereich.addClassName("profil-section");

        return bereich;
    }


    private void aenderePasswort(Benutzer benutzer) {

        String aktuell = aktuellesPasswortFeld.getValue();
        String neu = neuesPasswortFeld.getValue();
        String bestaetigung =
                passwortBestaetigenFeld.getValue();

        if (aktuell.isBlank()
                || neu.isBlank()
                || bestaetigung.isBlank()) {

            Notification.show(
                    getText("profil.password.required")
            );

            return;
        }

        if (!neu.equals(bestaetigung)) {

            passwortBestaetigenFeld.setInvalid(true);

            passwortBestaetigenFeld.setErrorMessage(
                    getText("profil.password.mismatch")
            );

            return;
        }

        passwortBestaetigenFeld.setInvalid(false);

        try {

            benutzerService.aenderePasswort(
                    benutzer,
                    aktuell,
                    neu
            );

            aktuellesPasswortFeld.clear();
            neuesPasswortFeld.clear();
            passwortBestaetigenFeld.clear();

            Notification.show(
                    getText("profil.password.success")
            );

        } catch (IllegalArgumentException e) {

            Notification.show(e.getMessage());
        }
    }

    private VerticalLayout erstelleKontoloeschBereich(
            Benutzer benutzer) {

        Button loeschenButton = new Button(
                getText("profil.delete.title"),
                event -> zeigeLoeschdialog(benutzer)
        );

        loeschenButton.addThemeVariants(
                ButtonVariant.LUMO_ERROR
        );

        VerticalLayout bereich = new VerticalLayout(
                new H2(getText("profil.delete.title")),
                new Paragraph(
                        getText("profil.delete.description")
                ),
                loeschenButton
        );

        bereich.setPadding(true);
        bereich.setSpacing(true);
        bereich.setWidthFull();

        bereich.addClassName("profil-delete-section");

        return bereich;
    }

    private void zeigeLoeschdialog(Benutzer benutzer) {

        Dialog dialog = new Dialog();

        dialog.setHeaderTitle(
                getText("profil.delete.dialog.title")
        );

        dialog.setWidth("450px");
        dialog.setMaxWidth("95vw");
        dialog.setCloseOnOutsideClick(false);

        PasswordField passwortFeld =
                new PasswordField(
                        getText("profil.delete.password")
                );

        passwortFeld.setWidthFull();

        Checkbox bestaetigung = new Checkbox(
                getText("profil.delete.confirmation")
        );

        Button abbrechenButton = new Button(
                getText("profil.delete.cancel"),
                event -> dialog.close()
        );

        Button endgueltigLoeschenButton = new Button(
                getText("profil.delete.submit"),
                event -> {

                    if (passwortFeld.isEmpty()) {

                        passwortFeld.setInvalid(true);

                        passwortFeld.setErrorMessage(
                                getText(
                                        "profil.delete.password.required"
                                )
                        );

                        return;
                    }

                    try {

                        benutzerService.loescheKonto(
                                benutzer,
                                passwortFeld.getValue()
                        );

                        dialog.close();

                        aktuellerBenutzerService.abmelden();

                        getUI().ifPresent(ui ->
                                ui.navigate(LoginView.class)
                        );

                        Notification.show(
                                getText("profil.delete.success")
                        );

                    } catch (IllegalArgumentException e) {

                        passwortFeld.setInvalid(true);

                        passwortFeld.setErrorMessage(
                                e.getMessage()
                        );
                    }
                }
        );

        endgueltigLoeschenButton.setEnabled(false);

        endgueltigLoeschenButton.addThemeVariants(
                ButtonVariant.LUMO_ERROR
        );

        bestaetigung.addValueChangeListener(event ->

                endgueltigLoeschenButton.setEnabled(
                        Boolean.TRUE.equals(event.getValue())
                )
        );

        HorizontalLayout aktionen = new HorizontalLayout(
                abbrechenButton,
                endgueltigLoeschenButton
        );

        aktionen.setWidthFull();

        aktionen.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        VerticalLayout inhalt = new VerticalLayout(
                new Paragraph(
                        getText("profil.delete.warning")
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

    private String getText(String schluessel) {

        return messageSource.getMessage(
                schluessel,
                null,
                Locale.GERMAN
        );
    }
}
