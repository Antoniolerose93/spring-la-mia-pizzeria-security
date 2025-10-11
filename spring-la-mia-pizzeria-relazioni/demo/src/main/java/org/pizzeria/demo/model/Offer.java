package org.pizzeria.demo.model;


import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private LocalDate offerStartDate;
    
    private LocalDate offerEndDate;
    
    private String note;

    @ManyToOne
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public LocalDate getOfferStartDate(){
        return offerStartDate;
    }

    public void setOfferStartDate(LocalDate offerStartDate){
        this.offerStartDate = offerStartDate;
    }   

      public LocalDate getOfferEndDate(){
        return offerEndDate;
    }

    public void setOfferEndDate(LocalDate offerEndDate){
        this.offerEndDate = offerEndDate;
    }   

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }

    public Pizza getPizza(){
        return pizza;
    }

    public void setPizza(Pizza pizza){
        this.pizza = pizza;
    }

}
