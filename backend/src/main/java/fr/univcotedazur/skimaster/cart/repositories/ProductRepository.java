package fr.univcotedazur.skimaster.cart.repositories;

import fr.univcotedazur.skimaster.cart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
