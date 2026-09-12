package com.psyduck.myDesk.benutzerschnittstelle.layout;

import com.psyduck.myDesk.benutzerschnittstelle.ChatView;
import com.psyduck.myDesk.benutzerschnittstelle.DashboardView;
import com.psyduck.myDesk.benutzerschnittstelle.KalenderView;
import com.psyduck.myDesk.benutzerschnittstelle.LoginView;
import com.psyduck.myDesk.benutzerschnittstelle.PostfachView;
import com.psyduck.myDesk.benutzerschnittstelle.ToDoView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerSession;
import com.psyduck.myDesk.benutzerschnittstelle.FokusView;


public class MainLayout extends AppLayout {

    public MainLayout() {

        erstelleHeader();
        erstelleNavigation();
    }

    private void erstelleHeader() {

        DrawerToggle drawerToggle = new DrawerToggle();

        H1 titel = new H1("myDesk");

        titel.getStyle()
                .set("font-size", "var(--lumo-font-size-xl)")
                .set("margin", "0");

        Benutzer benutzer =
                BenutzerSession.getAktuellerBenutzer();

        String name = benutzer != null
                ? benutzer.getName()
                : "";

        Span begruessung = new Span(
                name.isEmpty()
                        ? ""
                        : "Hallo " + name
        );

        Button abmelden = new Button(
                "Abmelden",
                VaadinIcon.SIGN_OUT.create(),
                event -> {
                    BenutzerSession.abmelden();
                    UI.getCurrent().navigate(LoginView.class);
                }
        );

        HorizontalLayout header =
                new HorizontalLayout(
                        drawerToggle,
                        titel,
                        begruessung,
                        abmelden
                );

        header.setWidthFull();

        header.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        header.expand(titel);

        header.getStyle()
                .set("padding", "0 var(--lumo-space-m)")
                .set("box-sizing", "border-box");

        addToNavbar(header);
    }

    private void erstelleNavigation() {

        VerticalLayout navigation =
                new VerticalLayout();

        navigation.setPadding(true);
        navigation.setSpacing(false);
        navigation.setWidthFull();

        RouterLink dashboard = erstelleLink(
                "Dashboard",
                VaadinIcon.HOME,
                DashboardView.class
        );

        RouterLink postfach = erstelleLink(
                "Postfach",
                VaadinIcon.ENVELOPE,
                PostfachView.class
        );

        RouterLink chat = erstelleLink(
                "Chat",
                VaadinIcon.COMMENTS,
                ChatView.class
        );

        RouterLink kalender = erstelleLink(
                "Kalender",
                VaadinIcon.CALENDAR,
                KalenderView.class
        );

        RouterLink todo = erstelleLink(
                "To-Do",
                VaadinIcon.CHECK,
                ToDoView.class
        );
        
        RouterLink fokus = erstelleLink(
        	    "Fokus",
        	    VaadinIcon.TIMER,
        	    FokusView.class
        	);


        navigation.add(
                dashboard,
                postfach,
                chat,
                kalender,
                todo,
                fokus
        );

        addToDrawer(navigation);
    }

    private RouterLink erstelleLink(
            String text,
            VaadinIcon icon,
            Class<? extends com.vaadin.flow.component.Component> view
    ) {

        RouterLink link = new RouterLink();

        Span iconSpan = new Span(
                icon.create()
        );

        Span textSpan = new Span(text);

        HorizontalLayout layout =
                new HorizontalLayout(
                        iconSpan,
                        textSpan
                );

        layout.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        layout.setSpacing(true);

        link.add(layout);
        link.setRoute(view);

        link.getStyle()
                .set("padding", "var(--lumo-space-s)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("width", "100%")
                .set("box-sizing", "border-box");

        return link;
    }
}
