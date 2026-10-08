package com.psyduck.myDesk.benutzerschnittstelle;

import com.psyduck.myDesk.benutzerschnittstelle.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@StyleSheet("styles.css")
@Route(value = "chat", layout = MainLayout.class)
@PermitAll
public class ChatView extends VerticalLayout {
	public ChatView() {
	    setSizeFull();
	    setPadding(false);
	    setSpacing(false);
	    addClassName("chat-background");

	    VerticalLayout content = new VerticalLayout();
	    content.setSizeFull();
	    content.setPadding(true);
	    content.setSpacing(true);
	    content.addClassName("chat-content");

	    H2 titel = new H2("Chat");
	    titel.addClassName("chat-title");

	    Span untertitel = new Span("Tausche dich mit anderen aus.");
	    untertitel.addClassName("chat-subtitle");

	    VerticalLayout kopfbereich = new VerticalLayout(titel, untertitel);
	    kopfbereich.setPadding(false);
	    kopfbereich.setSpacing(false);

	    VerticalLayout chatPanel = erstelleChatPanel();

	    content.add(kopfbereich, chatPanel);
	    content.expand(chatPanel);
	    add(content);
	}

	private VerticalLayout erstelleChatPanel() {
	    VerticalLayout panel = new VerticalLayout();
	    panel.setSizeFull();
	    panel.setPadding(false);
	    panel.setSpacing(false);
	    panel.addClassName("chat-panel");

	    HorizontalLayout header = erstelleChatHeader();
	    VerticalLayout nachrichten = erstelleNachrichtenbereich();
	    HorizontalLayout eingabe = erstelleEingabebereich();

	    panel.add(header, nachrichten, eingabe);
	    panel.expand(nachrichten);

	    return panel;
	}

	private HorizontalLayout erstelleChatHeader() {
	    Span icon = new Span("chat");
	    icon.addClassName("material-symbols-rounded");
	    icon.addClassName("chat-header-icon");

	    Span titel = new Span("Chat");
	    titel.addClassName("chat-panel-title");

	    Span status = new Span("Bereit für Nachrichten");
	    status.addClassName("chat-panel-status");

	    VerticalLayout text = new VerticalLayout(titel, status);
	    text.setPadding(false);
	    text.setSpacing(false);

	    HorizontalLayout header = new HorizontalLayout(icon, text);
	    header.setWidthFull();
	    header.setPadding(true);
	    header.setSpacing(true);
	    header.setAlignItems(FlexComponent.Alignment.CENTER);
	    header.addClassName("chat-header");

	    return header;
	}

	private VerticalLayout erstelleNachrichtenbereich() {
	    VerticalLayout nachrichten = new VerticalLayout();
	    nachrichten.setSizeFull();
	    nachrichten.setPadding(true);
	    nachrichten.setSpacing(true);
	    nachrichten.addClassName("chat-messages");

	    Span leer = new Span("Noch keine Nachrichten");
	    leer.addClassName("chat-empty-title");

	    Span hinweis = new Span("Deine Chat-Unterhaltungen werden hier angezeigt.");
	    hinweis.addClassName("chat-empty-subtitle");

	    VerticalLayout leerBereich = new VerticalLayout(leer, hinweis);
	    leerBereich.setPadding(false);
	    leerBereich.setSpacing(false);
	    leerBereich.setAlignItems(FlexComponent.Alignment.CENTER);
	    leerBereich.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
	    leerBereich.setSizeFull();

	    nachrichten.add(leerBereich);
	    return nachrichten;
	}

	private HorizontalLayout erstelleEingabebereich() {
	    TextField eingabe = new TextField();
	    eingabe.setPlaceholder("Nachricht schreiben ...");
	    eingabe.setWidthFull();
	    eingabe.addClassName("chat-input");

	    Button senden = new Button("Senden", VaadinIcon.PAPERPLANE.create());
	    senden.addThemeVariants(ButtonVariant.PRIMARY);
	    senden.addClassName("chat-send-button");

	    HorizontalLayout bereich = new HorizontalLayout(eingabe, senden);
	    bereich.setWidthFull();
	    bereich.setPadding(true);
	    bereich.setSpacing(true);
	    bereich.setAlignItems(FlexComponent.Alignment.CENTER);
	    bereich.addClassName("chat-input-area");

	    return bereich;
	}

}