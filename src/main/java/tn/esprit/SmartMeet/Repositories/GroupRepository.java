package tn.esprit.SmartMeet.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.SmartMeet.models.Group;

public interface GroupRepository extends MongoRepository<Group, String> {
}
