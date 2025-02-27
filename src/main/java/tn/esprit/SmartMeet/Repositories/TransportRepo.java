package tn.esprit.SmartMeet.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.models.Transport;

@Repository
public interface TransportRepo extends MongoRepository<Transport, String> {
}
