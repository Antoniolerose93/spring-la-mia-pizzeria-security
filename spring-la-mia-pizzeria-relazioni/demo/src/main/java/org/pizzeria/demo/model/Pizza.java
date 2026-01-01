package org.pizzeria.demo.model;



import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "pizze")

public class Pizza {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@NotNull(message = "NOME cannot be null")
@Column(name="nome", nullable=false, unique=true)
private String nome;

@NotNull
@NotBlank(message ="descrizione obbligatoria")
private String descrizione;

@NotBlank(message ="url foto obbligatorio")
private String foto;

@NotNull
@Min(value=1)
private int prezzo;

@NotNull(message = "Seleziona almeno un ingrediente")
@Size(min = 1, message ="Devi selezionare almeno un ingrediente")
@ManyToMany //Una pizza può avere molti ingredienti e un ingrediente può appartanere a molte pizze
@JoinTable( //Serve a creare una tabella intermedia nel db che collega le due entità
    name = "pizza_ingredients", //nome della tabella di collegamento
    joinColumns = @JoinColumn(name = "pizza_id"), //colonna nella tabella intermedia che punta alla pizza
    inverseJoinColumns = @JoinColumn (name = "ingredient_id") //colonna nella tabella intermedia che punta all'ingrediente
)

private List <Ingredient> ingredients; //contiene tutti gli ingredienti associati ad una pizza


@OneToMany (mappedBy="pizza")
private List<Offer> offers;

public List <Ingredient> getIngredients(){
    return ingredients;
}

public void setIngredients(List<Ingredient> ingredients){
    this.ingredients = ingredients;
}


public Integer getId() {
        return id;
    }

public String getNome() {
    return nome;
}

public void setNome(String nome) {
    this.nome = nome;
}

public String getDescrizione() {
    return descrizione;
}

public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
}

public String getFoto(){
    return foto;
}

public void setFoto(String foto){
    this.foto = foto;
}

public int getPrezzo() {
    return prezzo;
}

public void setPrezzo(int prezzo) {
    this.prezzo = prezzo;
}

public void setId(int id) {
    this.id = id;
}


public List<Offer> getOffers() {
        return offers;
}

public void setOffers(List<Offer> offers) {
        this.offers = offers;
}


}
