package org.pizzeria.demo.controller;

import org.pizzeria.demo.model.Ingredient;
import org.pizzeria.demo.model.Pizza;
import org.pizzeria.demo.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;


@Controller
@RequestMapping("/ingredients")

public class Ingredientcontroller {

    @Autowired
    private IngredientRepository repository;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("ingredient", repository.findAll());
        model.addAttribute("ingredientObj", new Ingredient()); 
        //ingredientObj serve come contenitore.
        //E' un oggetto vuoto che permette di creare un nuovo ingrediente.

        return "ingredients/index";
        
}
    
    @PostMapping("/create")
    public String postMethodName (@Valid @ModelAttribute("ingredientObj") Ingredient ingredient,
                BindingResult bindingResult, Model model){
    Ingredient ing = repository.findByIngredient(ingredient.getIngredient()); //verifichiamo se esiste già un ingrediente con lo stesso nome di quello appena inserito
    if(ing == null) {
        //se non esiste un ingrediente con quel nome
    } else {
        //se esiste un ingrediente con quel nome
        bindingResult.addError(new ObjectError("ingredient", "Ingrediente già esistente"));
    
    }   

    if(bindingResult.hasErrors()) { //se ci sono errori non salva nulla e ritorna la pagina "ingredients/index"
        model.addAttribute("ingredienti", repository.findAll());
        return "ingredients/index";
    }

    repository.save(ingredient);
    return "redirect:/ingredients";

}

    @PostMapping("/delete/{id}")
    public String requestMethodName(@PathVariable Integer id, Model model) {
        Ingredient ing = repository.findById(id).get();
        for(Pizza pizza : ing.getPizze()) {
            pizza.getIngredients().remove(ing);
        }
        repository.deleteById(id);
        return "redirect:/ingredients";
    
    }

}
