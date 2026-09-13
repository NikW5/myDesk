package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        VerticalLayout loginBereich = new VerticalLayout();
        loginBereich.setWidthFull();
        loginBereich.setAlignItems(Alignment.CENTER);
        loginBereich.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout loginContainer = new VerticalLayout();
        loginContainer.setWidth("400px");
        loginContainer.setAlignItems(Alignment.CENTER);
        loginContainer.setSpacing(true);

        loginContainer.getStyle()
            .set("border", "1px solid lightgray")
            .set("padding", "20px")
            .set("background-color", "#f8f8f8");

        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login"); // WICHTIG für Spring Security

        loginContainer.add(loginForm);
        loginBereich.add(loginContainer);

        add(loginBereich);
        expand(loginBereich);

        add(erstelleGIFBereich());
    }

    private Component erstelleGIFBereich() {
        Image gif = new Image("images/psyduck.gif", "Psyduck");
        gif.setWidth("220px");
        gif.getStyle()
            .set("position", "fixed")
            .set("bottom", "40px")
            .set("left", "40px")
            .set("z-index", "1000");
        return gif;
    }
}

