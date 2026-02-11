package fr.univcotedazur.skimaster.order.repositories;

import fr.univcotedazur.skimaster.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
    select distinct o
    from Order o
    left join fetch o.lines l
    where o.customer.id = :customerId
    order by o.id desc
  """)
    List<Order> findAllByCustomerIdWithLines(@Param("customerId") Long customerId);
    List<Order> findByCustomerId(Long customerId);
    @Query("""
        select distinct o
        from Order o
        left join fetch o.lines
        where o.customer.id = :customerId and o.id = :orderId
    """)
    Optional<Order> findByCustomerIdAndIdWithLines(@Param("customerId") Long customerId, @Param("orderId") Long orderId);
}
