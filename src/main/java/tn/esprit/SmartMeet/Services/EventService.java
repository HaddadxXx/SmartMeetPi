package tn.esprit.SmartMeet.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.esprit.SmartMeet.Repositories.EventRepository;
import tn.esprit.SmartMeet.Repositories.SessionRepository;
import tn.esprit.SmartMeet.models.Event;
import tn.esprit.SmartMeet.models.Session;

import java.util.ArrayList;
import java.util.List;
@RequestMapping("/events")
@CrossOrigin
@Service
public class EventService implements IEventService {

    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository ;

    public EventService(EventRepository eventRepository, SessionRepository sessionRepository) {
        this.eventRepository = eventRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Event addEvenement(Event evenement){
        System.out.println("Tentative d'ajout dans la base : " + evenement);

        return eventRepository.save(evenement);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }



    @Override
    public Event updateEvent(String id, Event event) {
        Event existingEvent = eventRepository.findById(id).orElse(null);

        if (existingEvent != null) {
            // Mettre à jour les champs non nulls
            if (event.getNomEvent() != null) existingEvent.setNomEvent(event.getNomEvent());
            if (event.getTheme() != null) existingEvent.setTheme(event.getTheme());
            if (event.getDescription() != null) existingEvent.setDescription(event.getDescription());
            if (event.getTypeEvent() != null) existingEvent.setTypeEvent(event.getTypeEvent());
            if (event.getSessions() != null) existingEvent.setSessions(event.getSessions());

            return eventRepository.save(existingEvent);  // Sauvegarde l'événement mis à jour
        } else {
            throw new RuntimeException("Event not found with ID: " + id);
        }
    }

    @Override
    public Session ajouterSessionEtAffecterAEvenement(Session session, String eventName) {
        // Rechercher l'événement par son nom
        Event nomEvent = eventRepository.findByNomEvent(eventName);
        if (nomEvent == null) {
            throw new RuntimeException("Aucun événement trouvé avec le nom : " + eventName);
        }


        // Sauvegarder la session si elle n'existe pas encore
        session = sessionRepository.save(session);
        // Vérifier et initialiser la liste des sessions
        if (nomEvent.getSessions() == null) {
            nomEvent.setSessions(new ArrayList<>());
        }

        // Ajouter la session à l'événement
        nomEvent.getSessions().add(session);

        // Sauvegarder l'événement mis à jour
        eventRepository.save(nomEvent);

        return session;
    }
}
