package com.psyduck.myDesk.benutzerschnittstelle;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Route(value = "", autoLayout = false)
@AnonymousAllowed
public class RootView extends VerticalLayout
        implements BeforeEnterObserver {

    private final transient AuthenticationContext authContext;

    public RootView(AuthenticationContext authContext) {
        this.authContext = authContext;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        if (authContext.isAuthenticated()) {
            event.forwardTo(DashboardView.class);
        } else {
            event.forwardTo(LoginView.class);
        }
    }
}