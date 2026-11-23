
package com.psyduck.myDesk.benutzerschnittstelle;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Anhang;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.Nachricht;
import com.psyduck.myDesk.persistenz.NachrichtService;
import com.psyduck.myDesk.security.AktuellerBenutzerService;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import org.springframework.context.MessageSource;

import jakarta.annotation.security.PermitAll;

@StyleSheet("styles.css")
@Route(value = "postfach", layout = MainLayout.class)
@PermitAll
public class PostfachView extends VerticalLayout {

    private final NachrichtService nachrichtService;
    private final AktuellerBenutzerService aktuellerBenutzerService;
    private final MessageSource messageSource;

    private final Span neueNachrichtHinweis;
    private final Grid<Nachricht> grid;

    private boolean gesendeteNachrichtenAnzeigen = false;

    public PostfachView(
            NachrichtService nachrichtService,
            AktuellerBenutzerService aktuellerBenutzerService,
            MessageSource messageSource) {

        this.nachrichtService = nachrichtService;
        this.aktuellerBenutzerService = aktuellerBenutzerService;
        this.messageSource = messageSource;

        initialisiereLayout();

        neueNachrichtHinweis =
                erstelleNeueNachrichtHinweis();

        grid = erstelleNachrichtentabelle();

        MasterDetailLayout masterDetail =
                erstelleMasterDetailLayout();

        baueOberflaeche(masterDetail);

        aktualisiereNeueNachrichtHinweis(
                neueNachrichtHinweis
        );
    }

    private String getText(String schluessel) {
        return messageSource.getMessage(
                schluessel,
                null,
                schluessel,
                Locale.GERMAN
        );
    }

    private String getText(
            String schluessel,
            Object... argumente) {

        return messageSource.getMessage(
                schluessel,
                argumente,
                schluessel,
                Locale.GERMAN
        );
    }

    private void initialisiereLayout() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("postfach-background");
    }

    private void baueOberflaeche(
            MasterDetailLayout masterDetail) {

        VerticalLayout content = new VerticalLayout();

        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.addClassName("postfach-content");

        content.add(
                erstelleKopfbereich(),
                erstelleNachrichtenTabs(),
                erstelleToolbar(),
                neueNachrichtHinweis,
                masterDetail
        );

        content.expand(masterDetail);

        add(content);
    }

    private VerticalLayout erstelleKopfbereich() {

        H2 titel = new H2(
                getText("postfach.title")
        );

        titel.addClassName("postfach-title");

        Span untertitel = new Span(
                getText("postfach.subtitle")
        );

        untertitel.addClassName("postfach-subtitle");

        VerticalLayout kopfbereich =
                new VerticalLayout(titel, untertitel);

        kopfbereich.setPadding(false);
        kopfbereich.setSpacing(false);

        return kopfbereich;
    }


    private HorizontalLayout erstelleToolbar() {

        Button neueNachricht = new Button(
                getText("postfach.button.new"),
                VaadinIcon.EDIT.create(),
                event -> UI.getCurrent()
                        .navigate(NachrichtSendenView.class)
        );

        neueNachricht.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        Button aktualisieren = new Button(
                getText("postfach.button.refresh"),
                VaadinIcon.REFRESH.create(),
                event -> aktualisiereNachrichten()
        );

        aktualisieren.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY
        );

        HorizontalLayout toolbar =
                new HorizontalLayout(
                        neueNachricht,
                        aktualisieren
                );

        toolbar.setWidthFull();
        toolbar.setPadding(false);
        toolbar.setSpacing(true);

        toolbar.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        toolbar.addClassName("postfach-toolbar");

        return toolbar;
    }

    private Span erstelleNeueNachrichtHinweis() {

        Span hinweis = new Span(
                VaadinIcon.ENVELOPE.create(),
                new Span(getText(
                        "postfach.notification.unread.many"
                ))
        );

        hinweis.addClassName("postfach-notification");

        return hinweis;
    }

    private MasterDetailLayout erstelleMasterDetailLayout() {

        MasterDetailLayout layout =
                new MasterDetailLayout();

        layout.setSizeFull();
        layout.setExpandMaster(true);
        layout.setExpandDetail(true);
        layout.setDetailSize("420px");
        layout.setMaster(grid);
        layout.setDetail(null);
        layout.addClassName("postfach-master-detail");

        VerticalLayout details =
                erstelleDetailbereich(layout);

        grid.asSingleSelect().addValueChangeListener(event -> {

            Nachricht ausgewaehlt = event.getValue();

            if (ausgewaehlt == null) {
                layout.setDetail(null);
                return;
            }

            if (!ausgewaehlt.isGelesen()) {

                nachrichtService.alsGelesenMarkieren(
                        ausgewaehlt
                );

                grid.getDataProvider().refreshItem(
                        ausgewaehlt
                );

                aktualisiereNeueNachrichtHinweis(
                        neueNachrichtHinweis
                );
            }

            aktualisiereDetailbereich(
                    details,
                    ausgewaehlt
            );

            layout.setDetail(details);
        });

        return layout;
    }

    private VerticalLayout erstelleDetailbereich(
            MasterDetailLayout masterDetail) {

        VerticalLayout details = new VerticalLayout();

        details.setSizeFull();
        details.getStyle().set("overflow-y", "auto");
        details.setPadding(true);
        details.setSpacing(true);
        details.addClassName("postfach-detail");

        H2 titel = new H2(
                getText("postfach.detail.title")
        );

        titel.addClassName("postfach-detail-title");

        Button schliessen = new Button(
                VaadinIcon.CLOSE.create()
        );

        schliessen.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY_INLINE
        );

        schliessen.getElement().setAttribute(
                "aria-label",
                getText("postfach.detail.close")
        );

        schliessen.addClickListener(event -> {
            grid.asSingleSelect().clear();
            masterDetail.setDetail(null);
        });

        HorizontalLayout header =
                new HorizontalLayout(titel, schliessen);

        header.setWidthFull();
        header.setPadding(false);
        header.setSpacing(false);

        header.setJustifyContentMode(
                FlexComponent.JustifyContentMode.BETWEEN
        );

        header.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        header.addClassName("postfach-detail-header");

        details.add(header);

        return details;
    }

    private void aktualisiereDetailbereich(
            VerticalLayout details,
            Nachricht nachricht) {

        while (details.getComponentCount() > 1) {
            details.remove(details.getComponentAt(1));
        }

        TextField titel = new TextField(
                getText("postfach.detail.subject")
        );

        titel.setWidthFull();
        titel.setReadOnly(true);
        titel.setValue(nachricht.getTitel());
        titel.addClassName("postfach-detail-field");

        TextField von = new TextField(
                getText("postfach.detail.sender")
        );

        von.setWidthFull();
        von.setReadOnly(true);
        von.setValue(nachricht.getAbsender().getName());
        von.addClassName("postfach-detail-field");

        TextArea inhalt = new TextArea(
                getText("postfach.detail.message")
        );

        inhalt.setWidthFull();
        inhalt.setHeight("300px");
        inhalt.setReadOnly(true);
        inhalt.setValue(nachricht.getInhalt());
        inhalt.addClassName("postfach-detail-message");

        Span anhangTitel = new Span(
                getText("postfach.attachments.title")
        );

        anhangTitel.addClassName(
                "postfach-attachment-title"
        );

        VerticalLayout anhaenge = new VerticalLayout();

        anhaenge.setPadding(false);
        anhaenge.setSpacing(true);
        anhaenge.addClassName("postfach-attachments");

        if (nachricht.getAnhaenge().isEmpty()) {

            Span leer = new Span(
                    getText("postfach.attachments.empty")
            );

            leer.addClassName(
                    "postfach-no-attachments"
            );

            anhaenge.add(leer);

        } else {

            for (Anhang anhang : nachricht.getAnhaenge()) {
                anhaenge.add(erstelleAnhang(anhang));
            }
        }

        Button antworten = new Button(
                getText("postfach.button.reply"),
                VaadinIcon.REPLY.create()
        );

        antworten.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        antworten.addClickListener(event ->
                UI.getCurrent().navigate(
                        NachrichtSendenView.class,
                        new QueryParameters(
                                Map.of(
                                        "antwortAuf",
                                        List.of(
                                                String.valueOf(
                                                        nachricht.getId()
                                                )
                                        )
                                )
                        )
                )
        );

        HorizontalLayout antwortBereich =
                new HorizontalLayout(antworten);

        antwortBereich.setWidthFull();

        antwortBereich.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        details.add(
                titel,
                von,
                inhalt,
                anhangTitel,
                anhaenge,
                antwortBereich
        );
    }

    private HorizontalLayout erstelleAnhang(Anhang anhang) {

        Span icon = new Span(
                VaadinIcon.PAPERCLIP.create()
        );

        icon.addClassName(
                "postfach-attachment-icon"
        );

        DownloadHandler downloadHandler =
                DownloadHandler.fromInputStream(
                        event -> new DownloadResponse(
                                new ByteArrayInputStream(
                                        anhang.getInhalt()
                                ),
                                anhang.getDateiname(),
                                anhang.getDateityp(),
                                anhang.getInhalt().length
                        )
                );

        Anchor download = new Anchor(
                downloadHandler,
                anhang.getDateiname()
        );

        download.addClassName(
                "postfach-attachment-link"
        );

        HorizontalLayout zeile =
                new HorizontalLayout(icon, download);

        zeile.setWidthFull();
        zeile.setPadding(false);
        zeile.setSpacing(true);

        zeile.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        zeile.addClassName("postfach-attachment");

        return zeile;
    }


    private void aktualisiereNachrichten() {

        grid.setItems(ladeNachrichten());
        grid.asSingleSelect().clear();

        aktualisiereNeueNachrichtHinweis(
                neueNachrichtHinweis
        );
    }

    private List<Nachricht> ladeNachrichten() {

        Benutzer benutzer =
                aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) {
            return List.of();
        }

        List<Nachricht> nachrichten;

        if (gesendeteNachrichtenAnzeigen) {

            nachrichten =
                    nachrichtService.getGesendeteNachrichten(
                            benutzer
                    );

        } else {

            nachrichten =
                    nachrichtService.getNachrichten(
                            benutzer
                    );
        }

        return nachrichten.stream()
                .sorted(
                        Comparator.comparing(
                                Nachricht::getEmpfangenAm
                        ).reversed()
                )
                .toList();
    }

    private void aktualisiereNeueNachrichtHinweis(
            Span neueNachrichtHinweis) {

        Benutzer benutzer =
                aktuellerBenutzerService.getAktuellerBenutzer();

        if (benutzer == null) {
            neueNachrichtHinweis.setVisible(false);
            return;
        }

        long anzahl =
                nachrichtService.getAnzahlUngeleseneNachrichten(
                        benutzer
                );

        boolean anzeigen =
                anzahl > 0 && !gesendeteNachrichtenAnzeigen;

        neueNachrichtHinweis.setVisible(anzeigen);

        if (anzahl == 1) {

            neueNachrichtHinweis.setText(
                    getText("postfach.notification.unread.one")
            );

        } else {

            neueNachrichtHinweis.setText(
                    getText("postfach.notification.unread.many")
            );
        }
    }

    private Grid<Nachricht> erstelleNachrichtentabelle() {

        Grid<Nachricht> grid =
                new Grid<>(Nachricht.class, false);

        grid.setSizeFull();
        grid.addClassName("postfach-grid");

        if (gesendeteNachrichtenAnzeigen) {

            grid.addColumn(
                    nachricht ->
                            nachricht.getEmpfaenger().getName()
            )
            .setHeader(
                    getText("postfach.grid.recipient")
            )
            .setSortable(true)
            .setComparator(
                    nachricht ->
                            nachricht.getEmpfaenger().getName()
            )
            .setFlexGrow(1);

        } else {

            grid.addComponentColumn(nachricht -> {

                Span absender = new Span(
                        nachricht.getAbsender().getName()
                );

                if (!nachricht.isGelesen()) {

                    absender.addClassName(
                            "postfach-unread-text"
                    );

                    Span indikator = new Span("●");

                    indikator.addClassName(
                            "postfach-unread-indicator"
                    );

                    HorizontalLayout layout =
                            new HorizontalLayout(
                                    indikator,
                                    absender
                            );

                    layout.setPadding(false);
                    layout.setSpacing(true);

                    layout.setAlignItems(
                            FlexComponent.Alignment.CENTER
                    );

                    return layout;
                }

                return absender;
            })
            .setHeader(
                    getText("postfach.grid.sender")
            )
            .setSortable(true)
            .setComparator(
                    nachricht ->
                            nachricht.getAbsender().getName()
            )
            .setFlexGrow(1);
        }

        grid.addComponentColumn(nachricht -> {

            Span titel = new Span(
                    nachricht.getTitel()
            );

            if (!gesendeteNachrichtenAnzeigen
                    && !nachricht.isGelesen()) {

                titel.addClassName(
                        "postfach-unread-text"
                );
            }

            return titel;
        })
        .setHeader(
                getText("postfach.grid.subject")
        )
        .setSortable(true)
        .setComparator(Nachricht::getTitel)
        .setFlexGrow(2);

        grid.addComponentColumn(nachricht -> {

            Span vorschau = new Span(
                    nachricht.getVorschau()
            );

            vorschau.addClassName(
                    "postfach-preview"
            );

            return vorschau;
        })
        .setHeader(
                getText("postfach.grid.preview")
        )
        .setFlexGrow(3);

        grid.addComponentColumn(nachricht ->
                formatiereDatumUndUhrzeit(
                        nachricht.getEmpfangenAm()
                )
        )
        .setHeader(
                getText(
                        gesendeteNachrichtenAnzeigen
                                ? "postfach.grid.sent"
                                : "postfach.grid.received"
                )
        )
        .setSortable(true)
        .setComparator(Nachricht::getEmpfangenAm)
        .setFlexGrow(2);

        grid.setItems(ladeNachrichten());

        return grid;
    }


    private HorizontalLayout formatiereDatumUndUhrzeit(
            LocalDateTime datum) {

        LocalDate heute = LocalDate.now();

        String tag;

        if (datum.toLocalDate().equals(heute)) {

            tag = getText("postfach.date.today");

        } else if (
                datum.toLocalDate().equals(heute.minusDays(1))
        ) {

            tag = getText("postfach.date.yesterday");

        } else {

            tag = datum.format(
                    DateTimeFormatter.ofPattern(
                            "dd.MM.yyyy",
                            Locale.GERMAN
                    )
            );
        }

        Span tagSpan = new Span(tag);
        tagSpan.addClassName("postfach-date");

        Span uhrzeit = new Span(
                datum.format(
                        DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.GERMAN
                        )
                )
        );

        uhrzeit.addClassName("postfach-time");

        HorizontalLayout layout =
                new HorizontalLayout(tagSpan, uhrzeit);

        layout.setPadding(false);
        layout.setSpacing(false);

        layout.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        return layout;
    }

    private Tabs erstelleNachrichtenTabs() {

        Tab empfangen = new Tab(
                getText("postfach.tabs.received")
        );

        Tab gesendet = new Tab(
                getText("postfach.tabs.sent")
        );

        Tabs tabs = new Tabs(empfangen, gesendet);

        tabs.setWidthFull();

        tabs.addSelectedChangeListener(event -> {

            gesendeteNachrichtenAnzeigen =
                    event.getSelectedTab() == gesendet;

            aktualisiereNachrichten();
        });

        return tabs;
    }
}
