package tn.esprit.SmartMeet.Repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import tn.esprit.SmartMeet.models.ERole;
import tn.esprit.SmartMeet.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
