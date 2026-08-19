package com.psyduck.myDesk.benutzerschnittstelle.layout;

import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Location;

public class MainLayout extends VerticalLayout implements RouterLayout {

    private Kopfzeile kopfzeile;
    private final Div content;
    private final Fußzeile fußzeile;

    public MainLayout() {

        kopfzeile = new Kopfzeile(Kopfzeilentyp.DASHBOARD);
        content = new Div();
        fußzeile = new Fußzeile();

        // MainLayout nimmt die gesamte verfügbare Höhe ein
        setSizeFull();

        // Kein zusätzlicher Abstand zwischen den Bereichen
        setPadding(false);
        setSpacing(false);

        // Der Content-Bereich nimmt die gesamte verfügbare Breite ein
        content.setWidthFull();

        // Header, Content und Footer
        add(kopfzeile, content, fußzeile);

        // Content nimmt den übrigen Platz ein
        expand(content);
    }
    
    private Kopfzeilentyp ermittleKopfzeilentyp() {
    	Location location = UI.getCurrent()
                .getInternals()
                .getActiveViewLocation();

        String pfad = location.getPath();
        
        return switch (pfad) {
        	case "login" -> Kopfzeilentyp.LOGIN;
            case "dashboard" -> Kopfzeilentyp.DASHBOARD;
            case "postfach" -> Kopfzeilentyp.POSTFACH;
            case "neue_nachricht" -> Kopfzeilentyp.NACHRICHT_SENDEN;
            case "chat" -> Kopfzeilentyp.CHAT;
            case "kalender" -> Kopfzeilentyp.KALENDER;
            case "todo" -> Kopfzeilentyp.TODO;
            default -> Kopfzeilentyp.DASHBOARD;
        };
    }
    
    private void setKopfzeilentyp(Kopfzeilentyp typ) {

        remove(kopfzeile);

        kopfzeile = new Kopfzeile(typ);

        addComponentAsFirst(kopfzeile);
    }

    @Override
    public void showRouterLayoutContent(HasElement view) {
    	setKopfzeilentyp(ermittleKopfzeilentyp());
        content.getElement().appendChild(view.getElement());
    }

    @Override
    public void removeRouterLayoutContent(HasElement view) {
        content.getElement().removeChild(view.getElement());
    }
}
