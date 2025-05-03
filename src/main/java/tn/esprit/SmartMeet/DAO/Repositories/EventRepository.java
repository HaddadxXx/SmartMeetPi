package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Entities.User;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository< Event, String> {

    @Aggregation(pipeline = {
            "{ $match: { user: { $ne: null } } }",
            "{ $group: { _id: '$user' } }"
    })
    List<String> findDistinctUserIdsWithEvents();
    Event findByNomEvent(String eventName);

   // Event findByIdEvent(String eventId);

   // List<Event> findByOwnerId(String ownerId);
}
