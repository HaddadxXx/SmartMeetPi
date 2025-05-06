package tn.esprit.SmartMeet.DAO.Repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.SmartMeet.DAO.Entities.UserRecommendation;

public interface UserRecommendationRepository extends MongoRepository<UserRecommendation, String> {
    // Optionnel : méthode pour remplacer un document par clé userId
}