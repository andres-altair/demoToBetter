package com.andres.demotobetter.modules.security.infrastructure.security; // Nueva ubicación

import java.util.Collection;
import java.util.stream.Stream;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.andres.demotobetter.modules.security.domain.model.UserSecurity;

public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UserSecurity user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.active = user.isActive();
        
        this.authorities = user.getRoles().stream().flatMap(role -> {
            Stream<GrantedAuthority> roleAuthority = Stream.of(new SimpleGrantedAuthority(role.getName()));
            
            Stream<GrantedAuthority> permissionAuthorities = role.getPermissions().stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getName()));
            
            return Stream.concat(roleAuthority, permissionAuthorities);
        }).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
