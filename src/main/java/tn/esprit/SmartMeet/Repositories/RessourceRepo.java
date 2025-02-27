package tn.esprit.SmartMeet.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.models.Ressource;

@Repository
public interface RessourceRepo extends MongoRepository<Ressource, String> {
}
