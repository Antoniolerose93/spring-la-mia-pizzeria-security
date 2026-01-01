package org.pizzeria.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.security.PermitAll;

@Configuration
public class SecurityConfiguration {

    // private final DaoAuthenticationProvider authenticationProvider;

    // SecurityConfiguration(DaoAuthenticationProvider authenticationProvider) {
    //     this.authenticationProvider = authenticationProvider;
    // }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 //la sicurezza funziona con una catena di filtri per intercettare le richieste HTTP e decidere chi può accedere a cosa
 //se un utente è autenticato, se deve essere reindirizzato al login 
 //se ha i ruoli necessari ecc.
        http.authorizeHttpRequests() //qui stiao dicendo voglio definire le regole su chi può accedere a quali URL
                .requestMatchers("/pizze/create", "/pizze/edit/**").hasAuthority("ADMIN") //solo chi ha ruolo ADMIN può accedere a Books/edit ecc.
                .requestMatchers(HttpMethod.POST, "/pizze/**").hasAuthority("ADMIN")//** indica che qualsiasi cosa si trovi dopo Book/ si vede solo se sei admin
                .requestMatchers("/ingredients", "/ingredients/**").hasAuthority("ADMIN")
                .requestMatchers("/pizze", "/pizze/**").hasAnyAuthority("USER", "ADMIN")
                .requestMatchers("/**").permitAll() //indica che tutto il resto, ossia tutto ciò che non ha .hasAutorithy, deve essere permesso a tutto
                .and().formLogin() 
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout-success")
                .permitAll();
        return http.build();
    }

    @Bean //Rende un metodo come se fosse una classe
    DatabaseUserDetailsService userDetailsService() { //per autenticare un utente devo prendere questa classe che recupera l'utente da db
        return new DatabaseUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder() { //serve per capire se la password è corretta e quale algoritmo è stato usato per le password
        //
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); //la password non va direttamente a db ma viene criptata
        //a db la password è cifrata. L'algoritmo che cifra la password è lo stesso che viene usato per controllare la password a db
    }   //alla password l'algoritmo aggiunge un elemento, ad.es password_sale, serve per richiamare la cifratura. 
        //Quando l'utente inserisce la password, l'algoritmo deve aggiungere_sale per richiamare la password nel db.

    @Bean
    DaoAuthenticationProvider authenticationProvider() { //è un provider che imposta
        //il meccanismo di recupero dell'utente e il meccanismo di recupero della password
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService()); //gestiamo tutto ciò che ha a che fare con l'utente. 
        authProvider.setPasswordEncoder(passwordEncoder()); //gestiamo la password
        //con questo proveder definiamo che il form deve passare da questo servizio per valutare l'utente e la password.

        return authProvider;
    }
}
