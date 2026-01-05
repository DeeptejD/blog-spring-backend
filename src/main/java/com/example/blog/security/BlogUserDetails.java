package com.example.blog.security;

import com.example.blog.domain.entities.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * TL;DR: this bridges the gap between our applications 'User' and what Spring Security understands i.e. 'UserDetails'
 * UserDetails is like a contract between the user entity in our DB and spring security. Spring security only understands UserDetails hence we need to implement this interface and define a bunch of methods that spring security requires to carry out authentication and authorization.
 */
@Getter
@RequiredArgsConstructor
public class BlogUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // usually this is where you'd have the logic that decides which 'role' a logged-in user gets. In case there is ADMIN, USER etc. Here we only have a single role.
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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

    public UUID getId() {
        return user.getId();
    }
}
