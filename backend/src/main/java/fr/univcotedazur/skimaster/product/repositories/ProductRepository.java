package fr.univcotedazur.skimaster.product.repositories;

import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
}
