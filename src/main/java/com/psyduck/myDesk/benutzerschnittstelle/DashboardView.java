package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.psyduck.myDesk.persistenz.NachrichtService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(
	    value = "dashboard",
	    layout = MainLayout.class
	)
public class DashboardView extends VerticalLayout {

    private VerticalLayout bodyLayout = new VerticalLayout();

    public DashboardView() {

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        bodyLayout.setWidthFull();
        bodyLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        bodyLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        erstelleDashboardNavigation();
        add(bodyLayout);
        expand(bodyLayout);
    }

    private Component erstelleDashboardNavigation() {

        HorizontalLayout obereButtons = new HorizontalLayout();
        HorizontalLayout untereButtons = new HorizontalLayout();

        Button postfachButton = new Button(
        	    "Postfach (" + NachrichtService.getAnzahlNachrichten() + ")"
        	);
        Button chatButton = new Button("Chat");
        Button kalenderButton = new Button("Kalender");
        Button toDoButton = new Button("To-Dos");

        postfachButton.setWidth("200px");
        chatButton.setWidth("200px");
        kalenderButton.setWidth("200px");
        toDoButton.setWidth("200px");
        
        postfachButton.addClickListener(event -> UI.getCurrent().navigate(PostfachView.class));
        chatButton.addClickListener(event -> UI.getCurrent().navigate(ChatView.class));
        kalenderButton.addClickListener(event -> UI.getCurrent().navigate(KalenderView.class));
        toDoButton.addClickListener(event -> UI.getCurrent().navigate(ToDoView.class));

        obereButtons.add(postfachButton, chatButton);
        untereButtons.add(kalenderButton, toDoButton);

        obereButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        untereButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        bodyLayout.add(obereButtons, untereButtons);

        return bodyLayout;
    }
}
