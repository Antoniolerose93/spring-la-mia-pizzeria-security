// In alcuni momenti potremmo voler vendere le nostre pizze a un prezzo scontato.
// Dobbiamo quindi predisporre tutto il codice necessario per poter collegare un’offerta speciale a una pizza (in una relazione 1 a molti, cioè un’offerta speciale può essere collegata a una sola pizza, e una pizza può essere collegata a più offerte speciali).
// L’offerta speciale avrà :
// - una data di inizio
// - una data di fine
// - un titolo
// La pagina di dettaglio della singola pizza mostrerà l’elenco delle offerte collegate e avrà un bottone “Crea nuova offerta speciale” per aggiungerne una nuova.
// Accanto ad ogni offerta speciale è previsto un bottone che mi porterà a una pagina per modificarla.


package org.pizzeria.demo.controller;

import org.pizzeria.demo.model.Offer;
import org.pizzeria.demo.repository.OfferRepository;
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

    @PostMapping ("/create")
    public String create(@Valid @ModelAttribute("offer") Offer offer, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "offers/edit";
        }
        
        repository.save(offer);

        return "redirect:/pizze/show/" + offer.getPizza().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Integer id, Model model){
        Offer offer = repository.findById(id).get();
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



