package org.pizzeria.demo.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.pizzeria.demo.model.Role;
import org.pizzeria.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseUserDetails implements UserDetails {

    private String username;

    private String password;

    private Set<GrantedAuthority> authorities;   

    public DatabaseUserDetails (User user){ //tramite un utente recuperiamo i dati di username e password
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = new HashSet<>(); //Se un utente viene aggiunto più volte, HashSet lo ignora automaticamente, e ci sarà solo un SimpleGrantedAuthority per quel ruolo.
        for(Role role: user.getRoles()){
            SimpleGrantedAuthority sGA = new SimpleGrantedAuthority(role.getName()); //recuperiamo le autorizzazioni che ha
            this.authorities.add(sGA);
        }
    
    }

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername(){
        return username;
    }

}
