package com.psyduck.myDesk.security;

import com.psyduck.myDesk.persistenz.Benutzer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyDeskUserDetails implements UserDetails {

    private final Benutzer benutzer;

    public MyDeskUserDetails(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return benutzer.getPasswort();
    }

    @Override
    public String getUsername() {
        return benutzer.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
