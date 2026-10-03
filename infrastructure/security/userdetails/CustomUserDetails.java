package com.plazoleta.infrastructure.security.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;   // hash BCrypt; null cuando viene de un token
    private final String roleName;   // tal cual está en la BD
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String email, String password, String roleName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roleName = roleName;
        String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        this.authorities = List.of(new SimpleGrantedAuthority(authority));
    }

    public Long getId() { return id; }
    public String getRoleName() { return roleName; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}