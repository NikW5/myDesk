package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

@Route(
    value = "fokus",
    layout = MainLayout.class
)
public class FokusView extends VerticalLayout {

    private static final int FOKUS_DAUER = 25 * 60;
    private static final int KURZE_PAUSE_DAUER = 5 * 60;
    private static final int LANGE_PAUSE_DAUER = 15 * 60;

    private int verbleibendeSekunden =
        FOKUS_DAUER;

    private int pomodoroAnzahl = 0;

    private Span timerAnzeige;
    private Span modusAnzeige;
    private Span pomodoroAnzeige;

    private Button startPauseButton;
    private Button resetButton;

    private ScheduledExecutorService timerService;
    private ScheduledFuture<?> timerTask;

    public FokusView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titel =
            new H2("Fokus");

        Span untertitel =
            new Span(
                "Arbeite konzentriert und mache regelmäßige Pausen."
            );

        VerticalLayout kopf =
            new VerticalLayout(
                titel,
                untertitel
            );

        kopf.setPadding(false);
        kopf.setSpacing(false);

        Card timerKarte =
            erstelleTimerKarte();

        HorizontalLayout modusButtons =
            erstelleModusButtons();

        add(
            kopf,
            timerKarte,
            modusButtons
        );

        setAlignItems(
            FlexComponent.Alignment.CENTER
        );

        expand(timerKarte);
    }

    private Card erstelleTimerKarte() {

        modusAnzeige =
            new Span("Fokus");

        modusAnzeige.getStyle()
            .set(
                "font-size",
                "var(--lumo-font-size-l)"
            )
            .set(
                "font-weight",
                "600"
            )
            .set(
                "color",
                "var(--lumo-primary-color)"
            );

        timerAnzeige =
            new Span(
                formatiereZeit(
                    verbleibendeSekunden
                )
            );

        timerAnzeige.getStyle()
            .set(
                "font-size",
                "72px"
            )
            .set(
                "font-weight",
                "600"
            );

        pomodoroAnzeige =
            new Span(
                "Pomodoros: 0"
            );

        pomodoroAnzeige.getStyle()
            .set(
                "color",
                "var(--lumo-secondary-text-color)"
            );

        startPauseButton =
            new Button(
                "Start",
                VaadinIcon.PLAY.create()
            );

        startPauseButton.addThemeVariants(
            ButtonVariant.LUMO_PRIMARY
        );

        startPauseButton.addClickListener(
            event -> {

                if (timerLaeuft()) {
                    pausiereTimer();
                } else {
                    starteTimer();
                }
            }
        );

        resetButton =
            new Button(
                "Zurücksetzen",
                VaadinIcon.REFRESH.create()
            );

        resetButton.addClickListener(
            event -> setzeTimerZurueck()
        );

        HorizontalLayout buttons =
            new HorizontalLayout(
                startPauseButton,
                resetButton
            );

        buttons.setSpacing(true);

        VerticalLayout inhalt =
            new VerticalLayout(
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

        Card karte =
            new Card();

        karte.add(inhalt);

        karte.setWidth("500px");
        karte.setMinHeight("400px");

        return karte;
    }

    private HorizontalLayout erstelleModusButtons() {

        Button fokus =
            new Button(
                "Fokus",
                event ->
                    wechsleModus(
                        "Fokus",
                        FOKUS_DAUER
                    )
            );

        Button kurzePause =
            new Button(
                "Kurze Pause",
                event ->
                    wechsleModus(
                        "Kurze Pause",
                        KURZE_PAUSE_DAUER
                    )
            );

        Button langePause =
            new Button(
                "Lange Pause",
                event ->
                    wechsleModus(
                        "Lange Pause",
                        LANGE_PAUSE_DAUER
                    )
            );

        HorizontalLayout layout =
            new HorizontalLayout(
                fokus,
                kurzePause,
                langePause
            );

        layout.setJustifyContentMode(
            FlexComponent.JustifyContentMode.CENTER
        );

        return layout;
    }

    private void starteTimer() {

        if (timerLaeuft()) {
            return;
        }

        if (timerService == null ||
            timerService.isShutdown()) {

            timerService =
                Executors.newSingleThreadScheduledExecutor();
        }

        startPauseButton.setText("Pause");
        startPauseButton.setIcon(
            VaadinIcon.PAUSE.create()
        );

        timerTask =
            timerService.scheduleAtFixedRate(
                () -> {

                    getUI().ifPresent(ui ->
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
                            }
                        })
                    );

                },
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

        startPauseButton.setText("Start");
        startPauseButton.setIcon(
            VaadinIcon.PLAY.create()
        );
    }

    private void setzeTimerZurueck() {

        pausiereTimer();

        verbleibendeSekunden =
            FOKUS_DAUER;

        modusAnzeige.setText("Fokus");

        timerAnzeige.setText(
            formatiereZeit(
                verbleibendeSekunden
            )
        );
    }

    private void wechsleModus(
        String modus,
        int dauer
    ) {

        pausiereTimer();

        verbleibendeSekunden = dauer;

        modusAnzeige.setText(modus);

        timerAnzeige.setText(
            formatiereZeit(
                verbleibendeSekunden
            )
        );
    }

    private boolean timerLaeuft() {

        return timerTask != null &&
               !timerTask.isCancelled() &&
               !timerTask.isDone();
    }

    private String formatiereZeit(
        int sekunden
    ) {

        int minuten =
            sekunden / 60;

        int restSekunden =
            sekunden % 60;

        return String.format(
            "%02d:%02d",
            minuten,
            restSekunden
        );
    }

    private void zaehlePomodoro() {

        pomodoroAnzahl++;

        pomodoroAnzeige.setText(
            "Pomodoros: " + pomodoroAnzahl
        );
    }

    @Override
    protected void onDetach(
        DetachEvent event
    ) {

        pausiereTimer();

        if (timerService != null) {

            timerService.shutdownNow();
        }

        super.onDetach(event);
    }
}

