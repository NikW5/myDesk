package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("Dashboard")
public class DashboardView extends VerticalLayout{

	private Button loginButton = new Button("LoginPage");
	
	public DashboardView() {
		add(new H1("Dashboard"));
		add(loginButton);
		
		loginButton.addClickListener(event -> UI.getCurrent().navigate(LoginView.class));
		
	}
}