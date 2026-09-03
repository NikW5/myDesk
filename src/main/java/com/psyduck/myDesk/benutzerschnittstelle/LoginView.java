package com.psyduck.myDesk.benutzerschnittstelle;

import java.util.Optional;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;
import com.psyduck.myDesk.persistenz.BenutzerSession;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Image;

@Route(
	    value = "login",
	    layout = MainLayout.class
	)
public class LoginView extends VerticalLayout {

	private final BenutzerService benutzerService;

	public LoginView(BenutzerService benutzerService) {
	    this.benutzerService = benutzerService;

	    setSizeFull();
	    setPadding(true);
	    setSpacing(true);

	    VerticalLayout loginBereich = new VerticalLayout();
	    loginBereich.setWidthFull();
	    loginBereich.setAlignItems(Alignment.CENTER);
	    loginBereich.setJustifyContentMode(JustifyContentMode.CENTER);

	    loginBereich.add(erstelleLoginbereich());

	    add(loginBereich);
	    expand(loginBereich);
	    
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
        
        loginForm.addLoginListener(event -> {

        	Optional<Benutzer> benutzer = benutzerService.anmelden(
        	        event.getUsername(),
        	        event.getPassword());

        	if (benutzer.isPresent()) {

        	    BenutzerSession.setAktuellerBenutzer(
        	            benutzer.get()
        	    );

        	    getUI().ifPresent(ui ->
        	            ui.navigate(DashboardView.class)
        	    );

        	} else {

        	    loginForm.setError(true);
        	}

        });

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