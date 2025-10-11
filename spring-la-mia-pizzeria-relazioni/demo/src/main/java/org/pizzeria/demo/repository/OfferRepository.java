package org.pizzeria.demo.repository;

import org.pizzeria.demo.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository <Offer, Integer> {

}
