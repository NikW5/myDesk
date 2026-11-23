
package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.context.MessageSource;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

//@Route(value = "fokus", layout = MainLayout.class)
//@PermitAll
public class FokusView extends VerticalLayout {

    private static final int FOKUS_DAUER = 25 * 60;
    private static final int KURZE_PAUSE_DAUER = 5 * 60;
    private static final int LANGE_PAUSE_DAUER = 15 * 60;

    private final MessageSource messageSource;

    private int verbleibendeSekunden = FOKUS_DAUER;
    private int pomodoroAnzahl = 0;

    private String aktuellerModus = "fokus.mode.focus";

    private Span timerAnzeige;
    private Span modusAnzeige;
    private Span pomodoroAnzeige;

    private Button startPauseButton;

    private ScheduledExecutorService timerService;
    private ScheduledFuture<?> timerTask;

    public FokusView(MessageSource messageSource) {

        this.messageSource = messageSource;

        initialisiereLayout();

        VerticalLayout kopf = erstelleKopfbereich();
        Card timerKarte = erstelleTimerKarte();
        HorizontalLayout modusButtons = erstelleModusButtons();

        add(kopf, timerKarte, modusButtons);

        setAlignItems(FlexComponent.Alignment.CENTER);
        expand(timerKarte);
    }

    private void initialisiereLayout() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        addClassName("fokus-background");
    }

    private VerticalLayout erstelleKopfbereich() {

        H2 titel = new H2(getText("fokus.title"));
        titel.addClassName("fokus-title");

        Span untertitel = new Span(
                getText("fokus.subtitle")
        );

        untertitel.addClassName("fokus-subtitle");

        VerticalLayout kopf = new VerticalLayout(
                titel,
                untertitel
        );

        kopf.setPadding(false);
        kopf.setSpacing(false);
        kopf.setAlignItems(FlexComponent.Alignment.CENTER);

        return kopf;
    }

    private Card erstelleTimerKarte() {

        modusAnzeige = new Span(
                getText(aktuellerModus)
        );

        modusAnzeige.addClassName("fokus-mode");

        timerAnzeige = new Span(
                formatiereZeit(verbleibendeSekunden)
        );

        timerAnzeige.addClassName("fokus-timer");

        pomodoroAnzeige = new Span(
                getPomodoroText()
        );

        pomodoroAnzeige.addClassName("fokus-pomodoro");

        startPauseButton = new Button(
                getText("fokus.start"),
                VaadinIcon.PLAY.create()
        );

        startPauseButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        startPauseButton.addClassName("fokus-start-button");

        startPauseButton.addClickListener(event -> {

            if (timerLaeuft()) {
                pausiereTimer();
            } else {
                starteTimer();
            }
        });

        Button resetButton = new Button(
                getText("fokus.reset"),
                VaadinIcon.REFRESH.create()
        );

        resetButton.addClassName("fokus-reset-button");

        resetButton.addClickListener(
                event -> setzeTimerZurueck()
        );

        HorizontalLayout buttons = new HorizontalLayout(
                startPauseButton,
                resetButton
        );

        buttons.setSpacing(true);
        buttons.setJustifyContentMode(
                FlexComponent.JustifyContentMode.CENTER
        );

        VerticalLayout inhalt = new VerticalLayout(
                modusAnzeige,
                timerAnzeige,
                buttons,
                pomodoroAnzeige
        );

        inhalt.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        inhalt.setJustifyContentMode(
                FlexComponent.JustifyContentMode.CENTER
        );

        inhalt.setSpacing(true);
        inhalt.setPadding(true);
        inhalt.setSizeFull();

        Card karte = new Card();
        karte.add(inhalt);
        karte.setWidth("500px");
        karte.setMinHeight("400px");
        karte.addClassName("fokus-card");

        return karte;
    }

    private HorizontalLayout erstelleModusButtons() {

        Button fokus = new Button(
                getText("fokus.mode.focus"),
                event -> wechsleModus(
                        "fokus.mode.focus",
                        FOKUS_DAUER
                )
        );

        Button kurzePause = new Button(
                getText("fokus.mode.short"),
                event -> wechsleModus(
                        "fokus.mode.short",
                        KURZE_PAUSE_DAUER
                )
        );

        Button langePause = new Button(
                getText("fokus.mode.long"),
                event -> wechsleModus(
                        "fokus.mode.long",
                        LANGE_PAUSE_DAUER
                )
        );

        fokus.addClassName("fokus-mode-button");
        kurzePause.addClassName("fokus-mode-button");
        langePause.addClassName("fokus-mode-button");

        HorizontalLayout layout = new HorizontalLayout(
                fokus,
                kurzePause,
                langePause
        );

        layout.setJustifyContentMode(
                FlexComponent.JustifyContentMode.CENTER
        );

        layout.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        layout.getStyle().set("flex-wrap", "wrap");

        return layout;
    }

    private void starteTimer() {

        if (timerLaeuft() || verbleibendeSekunden <= 0) {
            return;
        }

        if (timerService == null || timerService.isShutdown()) {

            timerService =
                    Executors.newSingleThreadScheduledExecutor();
        }

        startPauseButton.setText(
                getText("fokus.pause")
        );

        startPauseButton.setIcon(
                VaadinIcon.PAUSE.create()
        );

        timerTask = timerService.scheduleAtFixedRate(

                () -> getUI().ifPresent(ui ->

                        ui.access(() -> {

                            if (verbleibendeSekunden > 0) {

                                verbleibendeSekunden--;

                                timerAnzeige.setText(
                                        formatiereZeit(
                                                verbleibendeSekunden
                                        )
                                );

                            } else {

                                pausiereTimer();

                                if ("fokus.mode.focus".equals(aktuellerModus)) {
                                    zaehlePomodoro();
                                }

                                getUI().ifPresent(ui2 ->
                                        ui2.push()
                                );
                            }
                        })
                ),

                1,
                1,
                TimeUnit.SECONDS
        );
    }

    private void pausiereTimer() {

        if (timerTask != null) {

            timerTask.cancel(false);
            timerTask = null;
        }

        startPauseButton.setText(
                getText("fokus.start")
        );

        startPauseButton.setIcon(
                VaadinIcon.PLAY.create()
        );
    }

    private void setzeTimerZurueck() {

        pausiereTimer();

        verbleibendeSekunden = FOKUS_DAUER;
        aktuellerModus = "fokus.mode.focus";

        aktualisiereTimerAnzeige();
    }

    private void wechsleModus(String modus, int dauer) {

        pausiereTimer();

        verbleibendeSekunden = dauer;
        aktuellerModus = modus;

        aktualisiereTimerAnzeige();
    }

    private void aktualisiereTimerAnzeige() {

        modusAnzeige.setText(
                getText(aktuellerModus)
        );

        timerAnzeige.setText(
                formatiereZeit(verbleibendeSekunden)
        );
    }

    private boolean timerLaeuft() {

        return timerTask != null
                && !timerTask.isCancelled()
                && !timerTask.isDone();
    }

    private String formatiereZeit(int sekunden) {

        int minuten = sekunden / 60;
        int restSekunden = sekunden % 60;

        return String.format(
                "%02d:%02d",
                minuten,
                restSekunden
        );
    }

    private void zaehlePomodoro() {

        pomodoroAnzahl++;

        pomodoroAnzeige.setText(
                getPomodoroText()
        );
    }

    private String getPomodoroText() {

        return getText("fokus.pomodoros", pomodoroAnzahl);
    }

    private String getText(String schluessel) {

        return messageSource.getMessage(
                schluessel,
                null,
                Locale.GERMAN
        );
    }

    private String getText(String schluessel, Object... argumente) {

        return messageSource.getMessage(
                schluessel,
                argumente,
                Locale.GERMAN
        );
    }

    @Override
    protected void onDetach(DetachEvent event) {

        pausiereTimer();

        if (timerService != null) {
            timerService.shutdownNow();
            timerService = null;
        }

        super.onDetach(event);
    }
}
