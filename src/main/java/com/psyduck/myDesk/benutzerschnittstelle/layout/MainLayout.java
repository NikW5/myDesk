package com.psyduck.myDesk.benutzerschnittstelle.layout;

import com.psyduck.myDesk.persistenz.Kopfzeilentyp;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends VerticalLayout implements RouterLayout {

    private final Kopfzeile kopfzeile;
    private final Div content;
    private final Fußzeile fußzeile;

    public MainLayout() {

        kopfzeile = new Kopfzeile();
        content = new Div();
        fußzeile = new Fußzeile();

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        content.setWidthFull();
        content.setSizeFull();

        add(kopfzeile, content, fußzeile);

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
            default -> Kopfzeilentyp.LOGIN;
        };
    }

    @Override
    public void showRouterLayoutContent(HasElement view) {

        // Kopfzeile an die aktuelle Route anpassen
        kopfzeile.setTyp(ermittleKopfzeilentyp());

        // Neue View in den Hauptbereich einsetzen
        content.getElement().appendChild(view.getElement());
    }

    @Override
    public void removeRouterLayoutContent(HasElement view) {

        // Die alte View vollständig von ihrem Parent lösen.
        //
        // Das ist robuster als removeChild(), weil Vaadin selbst
        // dafür sorgt, dass das Element aus seinem aktuellen Parent
        // entfernt wird.
        view.getElement().removeFromParent();
    }
}
