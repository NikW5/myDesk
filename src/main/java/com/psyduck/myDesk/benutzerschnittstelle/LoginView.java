package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.H1;

@Route("")
public class LoginView extends VerticalLayout {

	private FormLayout pageLayout = new FormLayout();
	private Button dashboardButton = new Button("Dashboard");
	
	public LoginView() {
		add(new H1("Willkommen!"));
		add(erzeugeLoginBereich());
		add(dashboardButton);
		
		dashboardButton.addClickListener(event -> UI.getCurrent().navigate(DashboardView.class));
		
	}
	
		
	private Component erzeugeLoginBereich() {
		LoginForm loginForm = new LoginForm();
		pageLayout.add(loginForm);
		return pageLayout;
	}
	

}