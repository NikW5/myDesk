package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("registrieren")
@PageTitle("Registrieren")
public class RegistrierenView extends VerticalLayout {

    private final TextField vornameFeld = new TextField("Vorname");
    private final TextField nachnameFeld = new TextField("Nachname");
    private final EmailField emailFeld = new EmailField("E-Mail");
    private final PasswordField passwortFeld = new PasswordField("Passwort");
    private final PasswordField passwortBestaetigenFeld =
            new PasswordField("Passwort bestätigen");

    private final Button registrierenButton = new Button("Registrieren");
    private final Button zurueckButton = new Button("Zurück");

    public RegistrierenView() {
    	
        ansichtAufbauen();
        
        registrierenButton.addClickListener(event -> UI.getCurrent().navigate(DashboardView.class));
        zurueckButton.addClickListener(event -> UI.getCurrent().navigate(LoginView.class));
    }

    private void ansichtAufbauen() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout layout = new VerticalLayout();
    	layout.setWidth("700px");
    	layout.getStyle().set("border", "1px solid lightgray");
    	layout.getStyle().set("padding", "20px");
    	
        VerticalLayout formularContainer = new VerticalLayout();
        formularContainer.setWidth("400px");
        formularContainer.setPadding(true);
        formularContainer.setSpacing(true);
        formularContainer.setAlignItems(Alignment.CENTER);
        
        formularContainer.getStyle()
        .set("border", "1px solid lightgray")
        .set("border-radius", "4px")
        .set("padding", "20px")
        .set("background-color", "#f8f8f8");

        H2 ueberschrift = new H2("Registrieren");

        FormLayout formular = new FormLayout();
        formular.setWidthFull();

        formular.add(
                vornameFeld,
                nachnameFeld,
                emailFeld,
                passwortFeld,
                passwortBestaetigenFeld
        );

        formular.setColspan(vornameFeld, 2);
        formular.setColspan(nachnameFeld, 2);
        formular.setColspan(emailFeld, 2);
        formular.setColspan(passwortFeld, 2);
        formular.setColspan(passwortBestaetigenFeld, 2);

        HorizontalLayout buttonLeiste = new HorizontalLayout(
                registrierenButton,
                zurueckButton
        );

        Fußzeile fußzeile = new Fußzeile();
        
        add(formularContainer, fußzeile);

        formularContainer.add(
                ueberschrift,
                formular,
                buttonLeiste
        );
    }
}