package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.ArrayList;
import java.util.List;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Anhang;
import com.psyduck.myDesk.persistenz.AnhangRepository;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.psyduck.myDesk.persistenz.Nachricht;
import com.psyduck.myDesk.persistenz.NachrichtService;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;

import jakarta.annotation.security.PermitAll;

@StyleSheet("styles.css")
@Route(value = "neue_nachricht", layout = MainLayout.class)
@PageTitle("Neue Nachricht")
@PermitAll
public class NachrichtSendenView extends VerticalLayout
        implements BeforeEnterObserver {

    private final List<Anhang> anhaenge = new ArrayList<>();
    private final VerticalLayout anhangListe = new VerticalLayout();

    private final ComboBox<Benutzer> empfaenger =
            new ComboBox<>("An");

    private final TextField betreff =
            new TextField("Titel");

    private final TextArea nachricht =
            new TextArea("Neue Nachricht");

    private final BenutzerService benutzerService;
    private final NachrichtService nachrichtService;
    private final AnhangRepository anhangRepository;
    private final AktuellerBenutzerService aktuellerBenutzerService;

    public NachrichtSendenView(
            BenutzerService benutzerService,
            NachrichtService nachrichtService,
            AnhangRepository anhangRepository,
            AktuellerBenutzerService aktuellerBenutzerService) {

        this.benutzerService = benutzerService;
        this.nachrichtService = nachrichtService;
        this.anhangRepository = anhangRepository;
        this.aktuellerBenutzerService = aktuellerBenutzerService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("nachricht-background");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.addClassName("nachricht-content");

        H2 titel = new H2("Neue Nachricht");
        titel.addClassName("nachricht-title");

        Span untertitel = new Span(
                "Verfasse eine Nachricht und sende sie an einen anderen Benutzer."
        );
        untertitel.addClassName("nachricht-subtitle");

        VerticalLayout kopfbereich =
                new VerticalLayout(titel, untertitel);
        kopfbereich.setPadding(false);
        kopfbereich.setSpacing(false);

        VerticalLayout nachrichtenPanel =
                erstelleNachrichtenPanel();

        content.add(kopfbereich, nachrichtenPanel);
        content.expand(nachrichtenPanel);

        add(content);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String antwortAuf = event.getLocation()
                .getQueryParameters()
                .getParameters()
                .get("antwortAuf")
                .stream()
                .findFirst()
                .orElse(null);

        if (antwortAuf == null) {
            return;
        }

        try {
            Integer nachrichtId = Integer.valueOf(antwortAuf);

            Nachricht original =
                    nachrichtService.getNachricht(nachrichtId);

            if (original == null) {
                return;
            }

            empfaenger.setValue(original.getAbsender());

            String originalTitel = original.getTitel();

            if (originalTitel.startsWith("Re:")) {
                betreff.setValue(originalTitel);
            } else {
                betreff.setValue("Re: " + originalTitel);
            }

            String zitierterInhalt =
                    "\n\n--- Ursprüngliche Nachricht ---\n"
                    + "Von: "
                    + original.getAbsender().getName()
                    + "\n"
                    + "Titel: "
                    + original.getTitel()
                    + "\n\n"
                    + original.getInhalt();

            nachricht.setValue(zitierterInhalt);

        } catch (NumberFormatException ignored) {
            // Ungültige Nachrichten-ID -> normale neue Nachricht
        }
    }

    private VerticalLayout erstelleNachrichtenPanel() {
        VerticalLayout panel = new VerticalLayout();
        panel.setSizeFull();
        panel.setPadding(true);
        panel.setSpacing(true);
        panel.addClassName("nachricht-panel");

        VerticalLayout formular =
                erstelleNachrichtenbereich();

        VerticalLayout anhangbereich =
                erstelleAnhangbereich();

        HorizontalLayout aktionen =
                erstelleSendenbereich();

        panel.add(formular, anhangbereich, aktionen);
        panel.expand(formular);

        return panel;
    }

    private VerticalLayout erstelleNachrichtenbereich() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setWidthFull();

        empfaenger.setItems(benutzerService.getBenutzer());
        empfaenger.setItemLabelGenerator(Benutzer::getName);
        empfaenger.setWidthFull();
        empfaenger.addClassName("nachricht-field");

        betreff.setWidthFull();
        betreff.addClassName("nachricht-field");

        nachricht.setWidthFull();
        nachricht.setHeight("250px");
        nachricht.addClassName("nachricht-message");

        layout.add(
                empfaenger,
                betreff,
                nachricht
        );

        return layout;
    }

    private VerticalLayout erstelleAnhangbereich() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setWidthFull();

        Span anhangLabel = new Span("Anhänge");
        anhangLabel.addClassName("nachricht-attachment-title");

        Upload upload = new Upload(
                UploadHandler.inMemory((metadata, bytes) -> {
                    Anhang anhang = new Anhang(
                            metadata.fileName(),
                            metadata.contentType(),
                            bytes
                    );

                    anhaenge.add(anhang);
                    anhangListe.add(erstelleAnhang(anhang));
                })
        );

        upload.setMaxFiles(20);
        upload.setWidthFull();
        upload.addClassName("nachricht-upload");

        anhangListe.setPadding(false);
        anhangListe.setSpacing(true);
        anhangListe.setWidthFull();
        anhangListe.addClassName("nachricht-attachments");

        layout.add(
                anhangLabel,
                upload,
                anhangListe
        );

        return layout;
    }

    private Component erstelleAnhang(Anhang anhang) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName("nachricht-attachment");

        Span icon = new Span();
        icon.addClassName("material-symbols-rounded");
        icon.setText("attach_file");
        icon.addClassName("nachricht-attachment-icon");

        Span dateiname = new Span(anhang.getDateiname());
        dateiname.addClassName("nachricht-attachment-name");

        Button loeschen = new Button(
                VaadinIcon.TRASH.create(),
                event -> {
                    anhaenge.remove(anhang);
                    anhangListe.remove(layout);
                }
        );

        loeschen.addClassName("nachricht-attachment-delete");
        loeschen.setTooltipText("Anhang entfernen");

        layout.add(icon, dateiname, loeschen);
        layout.expand(dateiname);

        return layout;
    }

    private HorizontalLayout erstelleSendenbereich() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );
        layout.addClassName("nachricht-actions");

        Button abbrechen = new Button(
                "Abbrechen",
                VaadinIcon.CLOSE.create()
        );

        abbrechen.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY
        );
        abbrechen.addClassName("nachricht-cancel-button");

        abbrechen.addClickListener(event ->
                getUI().ifPresent(ui ->
                        ui.navigate(PostfachView.class)
                )
        );

        Button senden = new Button(
                "Senden",
                VaadinIcon.PAPERPLANE.create()
        );

        senden.addThemeVariants(
                ButtonVariant.PRIMARY
        );
        senden.addClassName("nachricht-send-button");

        senden.addClickListener(event ->
                sendeNachricht()
        );

        layout.add(abbrechen, senden);

        return layout;
    }

    private void sendeNachricht() {
        Benutzer absender =
                aktuellerBenutzerService.getAktuellerBenutzer();

        Benutzer empfaengerBenutzer =
                empfaenger.getValue();

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

        empfaenger.setInvalid(false);
        betreff.setInvalid(false);
        nachricht.setInvalid(false);

        Nachricht gespeicherteNachricht =
                nachrichtService.speichern(
                        absender,
                        empfaengerBenutzer,
                        betreff.getValue().trim(),
                        nachricht.getValue()
                );

        for (Anhang anhang : anhaenge) {
            anhang.setNachricht(gespeicherteNachricht);
            anhangRepository.save(anhang);
        }

        betreff.clear();
        nachricht.clear();
        empfaenger.clear();
        anhaenge.clear();
        anhangListe.removeAll();

        getUI().ifPresent(ui ->
                ui.getPage().executeJs(
                        "alert('Nachricht wurde erfolgreich gesendet.')"
                )
        );
    }
}