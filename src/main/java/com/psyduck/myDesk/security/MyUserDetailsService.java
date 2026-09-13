package com.psyduck.myDesk.security;

import org.springframework.stereotype.Service;

import com.psyduck.myDesk.persistenz.Benutzer;
import com.psyduck.myDesk.persistenz.BenutzerService;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final BenutzerService benutzerService;

    public MyUserDetailsService(BenutzerService benutzerService) {
        this.benutzerService = benutzerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Benutzer benutzer = benutzerService.findByName(username);
        if (benutzer == null) {
            throw new UsernameNotFoundException("Benutzer nicht gefunden: " + username);
        }

        return User.withUsername(benutzer.getName())
                .password(benutzer.getPasswort())   // BCrypt!
                .roles("USER")
                .build();
    }
}
