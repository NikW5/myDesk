package com.psyduck.myDesk.benutzerschnittstelle.layout;

import com.psyduck.myDesk.benutzerschnittstelle.ChatView;
import com.psyduck.myDesk.benutzerschnittstelle.DashboardView;
import com.psyduck.myDesk.benutzerschnittstelle.FokusView;
import com.psyduck.myDesk.benutzerschnittstelle.KalenderView;
import com.psyduck.myDesk.benutzerschnittstelle.PostfachView;
import com.psyduck.myDesk.benutzerschnittstelle.ToDoView;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.security.AktuellerBenutzerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

import jakarta.annotation.security.PermitAll;

@StyleSheet("styles.css")
@PermitAll
public class MainLayout extends AppLayout {

    private final AktuellerBenutzerService aktuellerBenutzerService;

    public MainLayout(
            AktuellerBenutzerService aktuellerBenutzerService) {

        this.aktuellerBenutzerService =
                aktuellerBenutzerService;

        erstelleHeader();
        erstelleNavigation();
    }

    private void erstelleHeader() {
        DrawerToggle drawerToggle = new DrawerToggle();

        Image logo = new Image("/images/logo.png", "myDesk");
        logo.setHeight("40px");

        Benutzer benutzer =
                aktuellerBenutzerService.getAktuellerBenutzer();

        String name = benutzer != null
                ? benutzer.getName()
                : "";

        Avatar avatar = new Avatar(name);

        Span benutzername = new Span(name);

        HorizontalLayout benutzerBereich =
                new HorizontalLayout(avatar, benutzername);

        benutzerBereich.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        benutzerBereich.setSpacing(true);
        
        Button abmelden = new Button(
                "Abmelden",
                VaadinIcon.SIGN_OUT.create(),
                event -> aktuellerBenutzerService.abmelden()
        );
        
        abmelden.addClassName("abmelden-button");
        
        Div spacer = new Div();

        HorizontalLayout header = new HorizontalLayout(
                drawerToggle,
                logo,
                spacer,
                benutzerBereich,
                abmelden
        );

        header.setWidthFull();
        header.setAlignItems(
                FlexComponent.Alignment.CENTER
        );
        header.expand(spacer);

        header.getStyle()
                .set("padding", "0 var(--lumo-space-m)")
                .set("box-sizing", "border-box");

        addToNavbar(header);
    }

    private void erstelleNavigation() {
        VerticalLayout navigation = new VerticalLayout();
        
        navigation.addClassName("main-drawer");

        navigation.setPadding(true);
        navigation.setSpacing(true);
        navigation.setWidthFull();

        RouterLink dashboard = erstelleLink(
                "Dashboard",
                "home",
                DashboardView.class
        );

        RouterLink postfach = erstelleLink(
                "Postfach",
                "mail",
                PostfachView.class
        );

        RouterLink chat = erstelleLink(
                "Chat",
                "chat",
                ChatView.class
        );

        RouterLink kalender = erstelleLink(
                "Kalender",
                "calendar_month",
                KalenderView.class
        );

        RouterLink todo = erstelleLink(
                "To-Do",
                "check_box",
                ToDoView.class
        );

        RouterLink fokus = erstelleLink(
                "Fokus",
                "timer",
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
            String iconName,
            Class<? extends Component> view) {

        RouterLink link = new RouterLink();

        Span icon = new Span(iconName);
        icon.addClassName("material-symbols-rounded");
        
        Span textSpan = new Span(text);

        HorizontalLayout layout = new HorizontalLayout(
                icon,
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
                .set(
                        "border-radius",
                        "var(--lumo-border-radius-m)"
                )
                .set("width", "100%")
                .set("box-sizing", "border-box");

        return link;
    }
}
