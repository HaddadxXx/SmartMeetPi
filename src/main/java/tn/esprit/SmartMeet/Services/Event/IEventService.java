package tn.esprit.SmartMeet.Services.Event;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import java.util.List;
import java.util.Optional;

public interface IEventService {
    List<Event> getAllEvents();
    Optional<Event> getEventById(String id);
    Event createEvent(Event event);
    Event updateEvent(String id, Event updatedEvent);
    void deleteEvent(String id);
    String assignTransportToEvent(String eventId, String transportId);
}
