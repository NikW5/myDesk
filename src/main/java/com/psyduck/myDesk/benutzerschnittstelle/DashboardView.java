package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("dashboard")
public class DashboardView extends VerticalLayout {

    private HorizontalLayout headerLayout = new HorizontalLayout();
    private VerticalLayout bodyLayout = new VerticalLayout();

    public DashboardView() {

        setSizeFull();

        Kopfzeile kopfzeile = new Kopfzeile(Kopfzeile.Typ.DASHBOARD);

        headerLayout.setPadding(true);
        headerLayout.setWidthFull();

        bodyLayout.setPadding(true);
        bodyLayout.setWidthFull();
        bodyLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        bodyLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        add(kopfzeile,
                headerLayout,
                erstelleDashboardNavigation());
        expand();
    }

    private Component erstelleDashboardNavigation() {

        HorizontalLayout obereButtons = new HorizontalLayout();
        HorizontalLayout untereButtons = new HorizontalLayout();

        Button nachrichtenButton = new Button("Nachrichten(2)");
        Button chatButton = new Button("Chat");
        Button kalenderButton = new Button("Kalender");
        Button toDoButton = new Button("To-Dos");

        nachrichtenButton.setWidth("200px");
        chatButton.setWidth("200px");
        kalenderButton.setWidth("200px");
        toDoButton.setWidth("200px");

        obereButtons.add(nachrichtenButton, chatButton);
        untereButtons.add(kalenderButton, toDoButton);

        obereButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        untereButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        bodyLayout.add(obereButtons, untereButtons);

        return bodyLayout;
    }
}
