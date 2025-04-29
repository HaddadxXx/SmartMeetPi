package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.DAO.Entities.*;

@Repository
public interface RessourceRepo extends MongoRepository<Ressource, String> {
}
