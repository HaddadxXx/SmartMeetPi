package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.DAO.Entities.Event;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository< Event, String> {


    Event findByNomEvent(String eventName);

    Event findByIdEvent(String eventId);

   // List<Event> findByOwnerId(String ownerId);
}
