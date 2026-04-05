package fr.univcotedazur.skimaster.order.repositories;

import fr.univcotedazur.skimaster.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        select distinct o
        from Order o
        left join fetch o.items i
        where o.customer.id = :customerId
        order by o.orderId desc
    """)
    List<Order> findAllByCustomerIdWithItems(@Param("customerId") Long customerId);

    // derived query correcte
    List<Order> findByCustomer_Id(Long customerId);

    @Query("""
        select distinct o
        from Order o
        left join fetch o.items i
        where o.customer.id = :customerId and o.orderId = :orderId
    """)
    Optional<Order> findByCustomerIdAndOrderIdWithItems(
            @Param("customerId") Long customerId,
            @Param("orderId") Long orderId
    );
    @Query("""
        select distinct o
        from Order o
        left join fetch o.items i
        where o.orderId = :orderId
    """)
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);

    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.customer.id = :customerId " +
            "AND o.price = :price")
    boolean existsRecentOrderForSuperCarte(
            @Param("customerId") Long customerId,
            @Param("price") double price
    );
}
