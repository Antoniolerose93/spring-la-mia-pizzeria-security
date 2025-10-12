package org.pizzeria.demo.security;

import java.util.Optional;

import org.pizzeria.demo.model.User;
import org.pizzeria.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //è un componente che va a prendere l'utente da db, collega l'oggetto creato in DatabaseUserDetails con l'utente del db
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //definiamo come recuperiamo l'utente,
    //ossia con repository da db ricercandolo per username
        Optional<User> userOpt = userRepo.findByUsername(username); //Verifichiamo la presenza di o l'assenza di un valore
        //evitando la NullPointerException
        if(userOpt.isPresent()) {
            return new DatabaseUserDetails(userOpt.get());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
        
    }


    
}
