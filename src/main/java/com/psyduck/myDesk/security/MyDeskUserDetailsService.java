package com.psyduck.myDesk.security;

import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyDeskUserDetailsService
        implements UserDetailsService {

    private final BenutzerRepository benutzerRepository;

    public MyDeskUserDetailsService(
            BenutzerRepository benutzerRepository) {

        this.benutzerRepository = benutzerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        Benutzer benutzer =
                benutzerRepository
                        .findByNameIgnoreCase(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Benutzer nicht gefunden"
                                )
                        );

        return new MyDeskUserDetails(benutzer);
    }
}
