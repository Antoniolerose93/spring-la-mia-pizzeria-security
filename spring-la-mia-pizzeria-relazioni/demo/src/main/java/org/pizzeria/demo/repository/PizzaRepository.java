package org.pizzeria.demo.repository;

import java.util.List;
import java.util.Optional;

import org.pizzeria.demo.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;


//Accedere al database senza scrivere SQL a mano

//Estendendo JpaRepository<Pizza, Integer>, erediti automaticamente un sacco di metodi pronti per lavorare con il database.

public interface PizzaRepository extends JpaRepository<Pizza, Integer> { //Pizza è la classe, Integer è la chiave
// dal momento in cui estende una interfaccia eredita i suoi comportamenti, siccome JpaRepository è un Bean, PizzaRepository 
//diventa un Bean per ereditarietà ed entra nel contesto di Spring. In questo modo abbiamo lasciato a Spring il controllo delle operazioni

    public List<Pizza> findByNomeContainingIgnoreCase(String nome); //Filtro per selezionare gli elementi dal nome

    public Optional<Pizza> findByNome(String nome);

}
