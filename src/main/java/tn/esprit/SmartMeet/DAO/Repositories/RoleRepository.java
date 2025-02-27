package tn.esprit.SmartMeet.DAO.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.DAO.Entities.ERole;
import tn.esprit.SmartMeet.DAO.Entities.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
