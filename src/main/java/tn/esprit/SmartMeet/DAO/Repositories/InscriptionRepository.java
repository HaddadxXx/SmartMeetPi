package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.SmartMeet.DAO.Entities.Inscription;

public interface InscriptionRepository extends MongoRepository<Inscription, String> {
}