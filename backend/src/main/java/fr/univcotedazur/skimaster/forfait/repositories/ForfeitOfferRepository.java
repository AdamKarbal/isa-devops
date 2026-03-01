package fr.univcotedazur.skimaster.forfait.repositories;

import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.ForfeitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForfeitOfferRepository extends JpaRepository<ForfeitOffer, Long> {

    Optional<ForfeitOffer> findByAgeCategoryAndPassType(AgeCategory ageCategory, PassType passType);

}
