package tn.esprit.SmartMeet.DAO.Repositories;

import tn.esprit.SmartMeet.DAO.Entities.SponsorshipRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SponsorshipRequestRepository extends MongoRepository<SponsorshipRequest, String> {
}
