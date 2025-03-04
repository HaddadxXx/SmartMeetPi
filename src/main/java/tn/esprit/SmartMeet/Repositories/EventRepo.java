package tn.esprit.SmartMeet.Repositories;

import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
@Repository
public interface EventRepo extends MongoRepository<Event, String> {

}
