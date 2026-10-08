package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

public class NachObenButton extends Button {
	
	public NachObenButton() {
		super(VaadinIcon.ARROW_UP.create());
		
		setTooltipText("Nach oben");
		addClassName("nach-oben-button");
	}
}
