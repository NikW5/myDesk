
package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("login")
public class LoginView extends VerticalLayout {

    private final MessageSource messageSource;

    public LoginView(MessageSource messageSource) {

        this.messageSource = messageSource;

        initialisiereLayout();

        HorizontalLayout obereLeiste =
                erstelleObereLeiste();

        VerticalLayout loginBereich =
                erstelleLoginBereich();

        Component gifBereich =
                erstelleGIFBereich();

        add(
                obereLeiste,
                loginBereich,
                gifBereich
        );

        expand(loginBereich);
    }

    private void initialisiereLayout() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        addClassName("login-background");
    }

    private HorizontalLayout erstelleObereLeiste() {

        Button registrierenButton = new Button(
                getText("login.register"),
                event -> UI.getCurrent().navigate(
                        RegistrierenView.class
                )
        );

        registrierenButton.addClassName(
                "login-register-button"
        );

        HorizontalLayout obereLeiste =
                new HorizontalLayout(
                        registrierenButton
                );

        obereLeiste.setWidthFull();

        obereLeiste.setPadding(false);

        obereLeiste.setJustifyContentMode(
                FlexComponent.JustifyContentMode.END
        );

        obereLeiste.addClassName(
                "login-header"
        );

        return obereLeiste;
    }

    private VerticalLayout erstelleLoginBereich() {

        VerticalLayout loginBereich =
                new VerticalLayout();

        loginBereich.setWidthFull();

        loginBereich.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        loginBereich.setJustifyContentMode(
                FlexComponent.JustifyContentMode.CENTER
        );

        loginBereich.add(
                erstelleLoginContainer()
        );

        loginBereich.addClassName(
                "login-content"
        );

        return loginBereich;
    }

    private Component erstelleLoginContainer() {

        VerticalLayout loginContainer =
                new VerticalLayout();

        loginContainer.setWidth("400px");
        loginContainer.setMaxWidth("95vw");

        loginContainer.setAlignItems(
                FlexComponent.Alignment.CENTER
        );

        loginContainer.setSpacing(true);
        loginContainer.setPadding(true);

        loginContainer.addClassName(
                "login-container"
        );

        LoginForm loginForm =
                new LoginForm();

        loginForm.setAction("login");

        loginForm.addClassName(
                "login-form"
        );

        loginContainer.add(
                loginForm
        );

        return loginContainer;
    }

    private Component erstelleGIFBereich() {

        Image gif = new Image(
                "images/psyduck.gif",
                getText("login.gif.alt")
        );

        gif.setWidth("220px");

        gif.addClassName(
                "login-gif"
        );

        return gif;
    }

    private String getText(String schluessel) {

        return messageSource.getMessage(
                schluessel,
                null,
                schluessel,
                Locale.GERMAN
        );
    }
}
