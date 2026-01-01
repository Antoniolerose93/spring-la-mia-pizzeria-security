package org.pizzeria.demo.controller;

import java.util.List;
import java.util.Optional;

import org.pizzeria.demo.model.Offer;
import org.pizzeria.demo.model.Pizza;
import org.pizzeria.demo.repository.IngredientRepository;
import org.pizzeria.demo.repository.OfferRepository;
import org.pizzeria.demo.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizze")
public class Pizzacontroller {
   
    @Autowired //con Autowired richiediamo il controllo delle operazioni di implementazioni dell'interfaccia PizzaRepository.
    //Autowired segna il punto di iniezione perchè la classe Pizzacontroller ha una dipendenza verso PizzaRepository.
    //
    private PizzaRepository repository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired 
    private IngredientRepository ingredientRepository;


    @GetMapping("/") //Questa annotazione risponde a richieste HTTP Get all'URL associato al controller
        public String index (Authentication auth, Model model, @RequestParam(name="keyword", required=false)String keyword) { 
    //index è il nome della pagina che deve essere renderizzata. 
    //Model model è un contenitore che serve a passare dati dal controller alla pagina.
    // List<Pizza> result = null;repository.findAll(); 
    // model.addAttribute("list", result);
    // return "/pizze/index";
    //Sopra uso PizzaRepository per recuperare tutte le pizze dal database, findAll è un metodo ereditato da JpaRepository
            
    //Qui uso invece un filtro per dire che se non mi viene passata una keyword voglio devono tornarmi tutti.
        List<Pizza> result = null;
        if (keyword == null || keyword.isBlank()){//isBlank verifica se la stringa della keyword è vuota o contiene solo spazi
            result = repository.findAll();
        } else {
            result = repository.findByNomeContainingIgnoreCase(keyword);
        } 
        
        model.addAttribute("list", result);
        model.addAttribute("username", auth.getName());
        return "pizze/index";       
}


    @GetMapping ("/show/{id}") 
//Pathvariable consente di rimanere nell'url aggiungendo un ID
//Come scegliere se usare il PathVariable o il query param? il query param consente di inserire nell'url un ? seguito dal parametro
//La differenza è di concetto. Se il filtro identifica solo una risorsa occorre il path, se invece il filtro restituisce più risorse è meglio usare il query param
    public String show(@PathVariable("id") Integer id, Model model){
        Optional <Pizza> optionalPizza = repository.findById(id);  
    //Optional è una classe di Java che rappresenta un valore che può essere presente o assente.
    //Mette a disposizione dei metodi, isEmpty e isPresent
    // model.addAttribute("pizza", optionalPizza.get());
        if (optionalPizza.isPresent()) { //Se la pizza esiste a DB
            model.addAttribute("pizza", optionalPizza.get());
            model.addAttribute("empty", false);
        } else {
            model.addAttribute ("empty", true);
    }
    
        return "/pizze/show";

}

    @GetMapping("/create")
    public String create (Model model) {
        model.addAttribute("pizza", new Pizza());
        model.addAttribute("allIngredients", ingredientRepository.findAll());
        return "/pizze/create";
}

    @PostMapping("/create")
    public String save(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pizza> optPizza = repository.findByNome(formPizza.getNome());
        if(optPizza.isPresent()) {
            //qui vuol dire che ha trovato una pizza con stesso nome su db
            bindingResult.addError(new ObjectError("nome", "Nome already present"));
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("allIngredients", ingredientRepository.findAll());
            return "/pizze/create";
        }

        repository.save(formPizza);
        redirectAttributes.addFlashAttribute("successMessage", "Pizza inserita con successo");
        return "redirect:/pizze";
    }

    @GetMapping("/edit/{id}")
        public String edit(@PathVariable ("id") Integer id, Model model) {
        Optional <Pizza> optPizza = repository.findById(id);
        Pizza pizza = optPizza.get();
        model.addAttribute("pizza", pizza);
        model.addAttribute("allIngredients", ingredientRepository.findAll());
        return "/pizze/edit";
}

    @PostMapping("/edit/{id}")
        public String update(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {    
        Pizza oldPizza = repository.findById(formPizza.getId()).get();
        // //inseriamo un blocco, non si può modificare il nome e la descrizione delle pizze
        // if(!oldPizza.getNome().equals(formPizza.getNome())) { //verifichiamo se la vecchia pizza si chiama come la nuova. ! è il not che si mette all'inizio
        //     bindingResult.addError(new ObjectError("name", "Cannot change the name"));
        // }
        // if(!oldPizza.getDescrizione().equals(formPizza.getDescrizione())) {
        //     bindingResult.addError(new ObjectError("description", "Cannot change description"));
        // }
         if (bindingResult.hasErrors()) {
            return "/pizze/edit";
        }    

        repository.save(formPizza); 
        //essendo questo repository uguale a quello sopra come fa a capire 
        //se sta creando un elemento nuovo o se ne sta modificando uno esistente?
        //come fa il repository a sapere se siamo in creazione o in modifica?
        //Quando prende l'elemento che gli passiamo tramite l'id, capisce che c'è già un elemento con quell'id, ed è quello che deve aggiornare.
        // nella creazione si crea un nuovo ID invece. Nella modifica dice quell'elemento con quell'id esiste già e quindi è quello che deve modificare.
        
        return "redirect:/pizze/";

    }

    @PostMapping("/delete/{id}")
        public String delete (@PathVariable("id") Integer id) {
        Pizza pizza = repository.findById(id).get();
        for (Offer offerToDelete : pizza.getOffers()) {
            offerRepository.delete(offerToDelete);
        }
        repository.deleteById(id);

        return "redirect:/pizze";
    }

    @GetMapping("/{id}/offer")
        public String offer(@PathVariable("id") Integer id, Model model) {
        Offer offer = new Offer();
        offer.setPizza(repository.findById(id).get());

        model.addAttribute("offer", offer);
        //Creazione nuova offerta
        model.addAttribute("editMode", false);
        return "/offers/edit";
    }

}
