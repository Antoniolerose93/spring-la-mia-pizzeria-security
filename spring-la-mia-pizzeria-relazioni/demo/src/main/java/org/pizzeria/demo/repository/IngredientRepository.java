package org.pizzeria.demo.repository;
import org.pizzeria.demo.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository <Ingredient, Integer> {

    public Ingredient findByIngredient(String ingredient);

}
