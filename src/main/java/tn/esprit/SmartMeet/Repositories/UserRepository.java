package tn.esprit.SmartMeet.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import tn.esprit.SmartMeet.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {




  Optional<User> findByEmail(String email); // Pour charger un utilisateur
  Boolean existsByEmail(String email); // Pour vérifier l'existence
}
