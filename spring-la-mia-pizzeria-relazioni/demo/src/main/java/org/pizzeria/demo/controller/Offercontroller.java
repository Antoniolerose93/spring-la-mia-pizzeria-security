// In alcuni momenti potremmo voler vendere le nostre pizze a un prezzo scontato.
// Dobbiamo quindi predisporre tutto il codice necessario per poter collegare un’offerta speciale a una pizza (in una relazione 1 a molti, cioè un’offerta speciale può essere collegata a una sola pizza, e una pizza può essere collegata a più offerte speciali).
// L’offerta speciale avrà :
// - una data di inizio
// - una data di fine
// - un titolo
// La pagina di dettaglio della singola pizza mostrerà l’elenco delle offerte collegate e avrà un bottone “Crea nuova offerta speciale” per aggiungerne una nuova.
// Accanto ad ogni offerta speciale è previsto un bottone che mi porterà a una pagina per modificarla.


package org.pizzeria.demo.controller;

import java.util.Optional;

import org.pizzeria.demo.model.Offer;
import org.pizzeria.demo.model.Pizza;
import org.pizzeria.demo.repository.OfferRepository;
import org.pizzeria.demo.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/offers")
public class Offercontroller {

    @Autowired
    private OfferRepository repository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @GetMapping("/create/{pizzaId}")
    public String newOffer(@PathVariable("pizzaId") Integer pizzaId, Model model){
        Optional <Pizza> optPizza = pizzaRepository.findById(pizzaId);
        if (optPizza.isEmpty()){
            //Se la pizza non esiste, torno alla lista delle pizze
            return "redirect:/pizze";
        }
        
        Offer offer = new Offer();
        offer.setPizza(optPizza.get()); //Associo la pizza all'offerta
        
        model.addAttribute("offer", offer);
        model.addAttribute("editMode", false); //modalità "creazione"
        return "offers/edit";

    }

    @PostMapping ("/create")
    public String create(@Valid @ModelAttribute("offer") 
    Offer offer, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", false);
            return "offers/edit";
        }

        Optional<Pizza> optPizza = pizzaRepository.findById(offer.getPizza().getId());
        if(optPizza.isEmpty()){
            return "redirect:/pizze";
        }
        
        offer.setPizza(optPizza.get());

        repository.save(offer);

        return "redirect:/pizze/show/" + offer.getPizza().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Integer id, Model model){
        
        Optional<Offer> optOffer = repository.findById(id);

        if (optOffer.isEmpty()){
            return "redirect:/offers";
        }
                
        Offer offer = optOffer.get();
        model.addAttribute("editMode", true);
        model.addAttribute("offer", offer);
        return "/offers/edit";
    }

    @PostMapping ("/edit/{id}")
    public String update (@Valid @ModelAttribute("offer") Offer offer, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "/offers/edit";
        }

        repository.save(offer);
        return "redirect:/pizze/show/" + offer.getPizza().getId();

    }

    @PostMapping ("/delete/{id}")
    public String delete(@PathVariable("id")Integer id) {
        repository.deleteById(id);
        return "redirect:/pizze";
    }

}



