package tn.esprit.SmartMeet.DAO.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.mongodb.repository.Query;
import tn.esprit.SmartMeet.DAO.Entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email); // Pour charger un utilisateur

  Boolean existsByEmail(String email); // Pour vérifier l'existence

  Optional<User> findById(String id);

  // Exemple : recherche par nom contenant une chaîne (case-insensitive)
  List<User> findByEmailContainingIgnoreCase(String keyword);

  @Query("{ '$or': [ " +
          "{ 'email': { $regex: ?0, $options: 'i' } }, " +
          "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
          "{ 'lastName': { $regex: ?0, $options: 'i' } } " +
          "] }")
  List<User> searchByEmailOrFirstNameOrLastName(String keyword);


}
