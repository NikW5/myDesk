package com.psyduck.myDesk.benutzerschnittstelle;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Anhang;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerSession;
import com.psyduck.myDesk.persistenz.Nachricht;
import com.psyduck.myDesk.persistenz.NachrichtService;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

@StyleSheet("styles.css")
@Route(
        value = "postfach",
        layout = MainLayout.class
)
public class PostfachView extends VerticalLayout {

    private final NachrichtService nachrichtService;

    private Span neueNachrichtHinweis;
    private Grid<Nachricht> grid;

    public PostfachView(NachrichtService nachrichtService) {

        this.nachrichtService = nachrichtService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        neueNachrichtHinweis =
                erstelleNeueNachrichtHinweis();

        grid =
                erstelleNachrichtentabelle();

        HorizontalLayout toolbar =
                erstelleToolbar();

        MasterDetailLayout layout =
                new MasterDetailLayout();

        layout.setExpandDetail(true);
        layout.setDetailSize("250px");
        layout.setExpandMaster(true);

        layout.setMaster(grid);
        layout.setDetail(null);

        VerticalLayout details =
                new VerticalLayout();

        details.setPadding(false);

        Button schliessenButton =
                new Button(
                        VaadinIcon.CLOSE.create()
                );

        schliessenButton.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY_INLINE
        );

        schliessenButton
                .getElement()
                .setAttribute(
                        "aria-label",
                        "Vorschau schließen"
                );

        schliessenButton.addClickListener(
                event -> {
                    grid.asSingleSelect().clear();
                    layout.setDetail(null);
                }
        );

        HorizontalLayout headerLayout =
                new HorizontalLayout(
                        new H2("Details"),
                        schliessenButton
                );

        headerLayout.setWidthFull();

        headerLayout.setJustifyContentMode(
                FlexComponent.JustifyContentMode.BETWEEN
        );

        headerLayout.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        TextField titel =
                new TextField("Titel");

        titel.setWidthFull();
        titel.setReadOnly(true);

        TextField von =
                new TextField("Von");

        von.setWidthFull();
        von.setReadOnly(true);

        TextArea nachricht =
                new TextArea("Nachricht");

        nachricht.setWidthFull();
        nachricht.setHeight("350px");
        nachricht.setReadOnly(true);

        VerticalLayout anhangBereich =
                new VerticalLayout();

        anhangBereich.setPadding(false);
        anhangBereich.setSpacing(true);

        Span anhangUeberschrift =
                new Span("Anhänge:");

        anhangBereich.add(
                anhangUeberschrift
        );

        details.add(
                headerLayout,
                titel,
                von,
                nachricht,
                anhangBereich
        );

        grid.asSingleSelect()
                .addValueChangeListener(event -> {

                    Nachricht ausgewaehlt =
                            event.getValue();

                    if (ausgewaehlt == null) {
                        layout.setDetail(null);
                        return;
                    }

                    if (!ausgewaehlt.isGelesen()) {

                        nachrichtService
                                .alsGelesenMarkieren(
                                        ausgewaehlt
                                );

                        grid.getDataProvider()
                                .refreshItem(
                                        ausgewaehlt
                                );

                        aktualisiereNeueNachrichtHinweis();
                    }

                    titel.setValue(
                            ausgewaehlt.getTitel()
                    );

                    von.setValue(
                            ausgewaehlt.getBenutzer()
                    );

                    nachricht.setValue(
                            ausgewaehlt.getInhalt()
                    );

                    anhangBereich.removeAll();

                    anhangBereich.add(
                            anhangUeberschrift
                    );

                    for (
                            Anhang anhang
                            : ausgewaehlt.getAnhaenge()
                    ) {

                        HorizontalLayout anhangZeile =
                                new HorizontalLayout();

                        Span icon =
                                new Span(
                                        VaadinIcon.PAPERCLIP.create()
                                );

                        DownloadHandler downloadHandler =
                                DownloadHandler.fromInputStream(
                                        downloadEvent ->
                                                new DownloadResponse(
                                                        new ByteArrayInputStream(
                                                                anhang.getInhalt()
                                                        ),
                                                        anhang.getDateiname(),
                                                        anhang.getDateityp(),
                                                        anhang.getInhalt().length
                                                )
                                );

                        Anchor download =
                                new Anchor(
                                        downloadHandler,
                                        anhang.getDateiname()
                                );

                        anhangZeile.add(
                                icon,
                                download
                        );

                        anhangBereich.add(
                                anhangZeile
                        );
                    }

                    layout.setDetail(details);
                });

        layout.setWidthFull();
        layout.setHeightFull();

        add(
                toolbar,
                neueNachrichtHinweis,
                layout
        );

        expand(layout);

        aktualisiereNeueNachrichtHinweis();
    }

    private HorizontalLayout erstelleToolbar() {

        Button neueNachricht =
                new Button(
                        "Neue Nachricht",
                        VaadinIcon.EDIT.create(),
                        event ->
                                UI.getCurrent()
                                        .navigate(
                                                NachrichtSendenView.class
                                        )
                );

        Button aktualisieren =
                new Button(
                        VaadinIcon.REFRESH.create(),
                        event ->
                                aktualisiereNachrichten()
                );

        HorizontalLayout toolbar =
                new HorizontalLayout(
                        neueNachricht,
                        aktualisieren
                );

        toolbar.setWidthFull();

        toolbar.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        toolbar.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        return toolbar;
    }

    private void aktualisiereNachrichten() {

        grid.setItems(
                nachrichtService
                        .getNachrichten(
                                BenutzerSession
                                        .getAktuellerBenutzer()
                        )
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        Nachricht::getEmpfangenAm
                                ).reversed()
                        )
                        .toList()
        );

        grid.asSingleSelect().clear();

        aktualisiereNeueNachrichtHinweis();
    }

    private Span erstelleNeueNachrichtHinweis() {

        Span hinweis =
                new Span(
                        VaadinIcon.ENVELOPE.create(),
                        new Span(
                                " Neue Nachricht vorhanden"
                        )
                );

        hinweis.getStyle()
                .set(
                        "color",
                        "var(--lumo-primary-color)"
                )
                .set(
                        "font-weight",
                        "600"
                )
                .set(
                        "background",
                        "var(--lumo-primary-color-10pct)"
                )
                .set(
                        "padding",
                        "var(--lumo-space-s) var(--lumo-space-m)"
                )
                .set(
                        "border-radius",
                        "var(--lumo-border-radius-m)"
                )
                .set(
                        "width",
                        "fit-content"
                );

        return hinweis;
    }

    private void aktualisiereNeueNachrichtHinweis() {

        Benutzer benutzer =
                BenutzerSession.getAktuellerBenutzer();

        if (benutzer == null) {
            neueNachrichtHinweis.setVisible(false);
            return;
        }

        boolean neueNachricht =
                nachrichtService
                        .hatNeueNachricht(benutzer);

        neueNachrichtHinweis.setVisible(
                neueNachricht
        );
    }

    private Grid<Nachricht>
    erstelleNachrichtentabelle() {

        Grid<Nachricht> grid =
                new Grid<>(
                        Nachricht.class,
                        false
                );

        grid.addColumn(
                nachricht ->
                        nachricht
                                .getAbsender()
                                .getName()
        )
        .setHeader("Absender")
        .setSortable(true)
        .setComparator(
                nachricht ->
                        nachricht
                                .getAbsender()
                                .getName()
        )
        .setFlexGrow(1);

        grid.addColumn(
                Nachricht::getTitel
        )
        .setHeader("Titel")
        .setSortable(true)
        .setFlexGrow(2);

        grid.addColumn(
                Nachricht::getVorschau
        )
        .setHeader("Vorschau")
        .setSortable(true)
        .setFlexGrow(3);

        grid.addComponentColumn(
                nachricht ->
                        formatiereDatumUndUhrzeit(
                                nachricht.getEmpfangenAm()
                        )
        )
        .setHeader("Empfangen am")
        .setSortable(true)
        .setComparator(
                Nachricht::getEmpfangenAm
        )
        .setFlexGrow(2);

        grid.setSizeFull();

        grid.setItems(
                nachrichtService
                        .getNachrichten(
                                BenutzerSession
                                        .getAktuellerBenutzer()
                        )
                        .stream()
                        .sorted(
                                Comparator.comparing(
                                        Nachricht::getEmpfangenAm
                                ).reversed()
                        )
                        .toList()
        );

        grid.addClassName(
                "postfach-grid"
        );

        return grid;
    }

    private HorizontalLayout
    formatiereDatumUndUhrzeit(
            LocalDateTime datum
    ) {

        LocalDate heute =
                LocalDate.now();

        String tag;

        String uhrzeit =
                datum.format(
                        DateTimeFormatter.ofPattern(
                                "HH:mm",
                                Locale.GERMAN
                        )
                );

        if (
                datum.toLocalDate()
                        .equals(heute)
        ) {

            tag = "heute";

        } else if (
                datum.toLocalDate()
                        .equals(
                                heute.minusDays(1)
                        )
        ) {

            tag = "gestern";

        } else {
            tag =datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN));
        }

        Span tagSpan =
                new Span(tag);

        tagSpan.setWidth("90px");

        Span uhrzeitSpan =
                new Span(uhrzeit);

        HorizontalLayout datumLayout =
                new HorizontalLayout(
                        tagSpan,
                        uhrzeitSpan
                );

        datumLayout.setSpacing(false);
        datumLayout.setPadding(false);

        datumLayout.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        return datumLayout;
    }
}
