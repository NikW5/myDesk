package com.psyduck.myDesk.security;

import com.psyduck.myDesk.persistenz.Benutzer;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.stereotype.Service;

@Service
public class AktuellerBenutzerService {

    private final AuthenticationContext authenticationContext;

    public AktuellerBenutzerService(
            AuthenticationContext authenticationContext) {

        this.authenticationContext = authenticationContext;
    }

    public Benutzer getAktuellerBenutzer() {

        return authenticationContext
                .getAuthenticatedUser(MyDeskUserDetails.class)
                .map(MyDeskUserDetails::getBenutzer)
                .orElse(null);
    }

    public void abmelden() {
        authenticationContext.logout();
    }
}
