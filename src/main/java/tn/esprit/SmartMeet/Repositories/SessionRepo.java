package tn.esprit.SmartMeet.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.models.Session;
@Repository
public interface SessionRepo extends MongoRepository<Session, String> {
}
