package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForfaitRepository extends JpaRepository<Forfait, Long> {

    @Query("""
        select f
        from Forfait f
        where f.customer.id = :customerId
        order by f.id asc
    """)
    Optional<Forfait> findFirstByCustomerId(@Param("customerId") Long customerId);

    @Query("""
        select f
        from Forfait f
        where f.customer.id = :customerId
        order by f.id asc
    """)
    List<Forfait> findAllByCustomerId(@Param("customerId") Long customerId);

    @Query("""
        select f
        from Forfait f
        where f.id = :id
          and f.customer.id = :customerId
    """)
    Optional<Forfait> findByIdAndCustomerId(
            @Param("id") Long id,
            @Param("customerId") Long customerId
    );
}
