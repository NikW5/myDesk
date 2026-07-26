package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    public LoginView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout loginContainer = new VerticalLayout();
        loginContainer.setWidth("400px");
        loginContainer.setAlignItems(Alignment.CENTER);
        loginContainer.setSpacing(true);

        loginContainer.getStyle().set("border", "1px solid lightgray");
        loginContainer.getStyle().set("padding", "20px");
        loginContainer.getStyle().set("background-color", "#f8f8f8");

        LoginForm loginForm = new LoginForm();

        loginContainer.add(
                loginForm
        );
        
        Image logo = new Image("images/psyduck.gif", "Psyduck");

        logo.setWidth("220px");

        logo.getStyle()
            .set("position", "fixed")
            .set("bottom", "40px")
            .set("left", "40px");

        add(loginContainer, logo);

    }
}