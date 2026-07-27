package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Image;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();
        setPadding(true);

        Kopfzeile kopfzeile = new Kopfzeile(Kopfzeile.Typ.LOGIN);
        kopfzeile.setWidthFull();

        VerticalLayout loginBereich = new VerticalLayout();
        loginBereich.setSizeFull();
        loginBereich.setAlignItems(Alignment.CENTER);
        loginBereich.setJustifyContentMode(JustifyContentMode.CENTER);

        loginBereich.add(erstelleLoginbereich());

        add(kopfzeile, loginBereich);

        add(erstelleGIFBereich());
    }

    private Component erstelleLoginbereich() {
        VerticalLayout loginContainer = new VerticalLayout();

        loginContainer.setWidth("400px");
        loginContainer.setAlignItems(Alignment.CENTER);
        loginContainer.setSpacing(true);

        loginContainer.getStyle()
                .set("border", "1px solid lightgray")
                .set("padding", "20px")
                .set("background-color", "#f8f8f8");

        LoginForm loginForm = new LoginForm();

        loginContainer.add(loginForm);

        return loginContainer;
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