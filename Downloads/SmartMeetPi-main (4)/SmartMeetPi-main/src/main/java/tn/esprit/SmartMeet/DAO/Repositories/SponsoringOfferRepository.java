package tn.esprit.SmartMeet.DAO.Repositories;

import tn.esprit.SmartMeet.DAO.Entities.SponsoringOffer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsoringOfferRepository extends MongoRepository<SponsoringOffer, String> {
    public List<SponsoringOffer> findByEventId(String eventId);


}
