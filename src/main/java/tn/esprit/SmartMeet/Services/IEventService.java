package tn.esprit.SmartMeet.Services;

import tn.esprit.SmartMeet.models.Event;
import tn.esprit.SmartMeet.models.Session;

import java.util.List;

public interface IEventService {

    Event addEvenement(Event evenement) ;
    List<Event> getAllEvents();
    Event getEventById(String id);
    void deleteEvent(String id);
    Event updateEvent(String id, Event event);
    public Session ajouterSessionEtAffecterAEvenement(Session session, String eventName);
}
