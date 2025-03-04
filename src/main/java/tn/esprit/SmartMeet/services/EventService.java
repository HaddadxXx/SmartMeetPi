package tn.esprit.SmartMeet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.Repositories.*;
import tn.esprit.SmartMeet.models.Event;
import tn.esprit.SmartMeet.models.Transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepo eventRepository;
    @Autowired
    private  TransportRepo transportRepository;


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(String id, Event updatedEvent) {
        updatedEvent.setId(id);
        return eventRepository.save(updatedEvent);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    public Event addTransportToEvent(String eventId, String transportId) {
        // Récupérer l'événement
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        // Récupérer l'objet Transport
        Transport transport = transportRepository.findById(transportId)
                .orElseThrow(() -> new RuntimeException("Transport non trouvé"));

        // Vérifier si la liste transports est initialisée
        if (event.getTransports() == null) {
            event.setTransports(new ArrayList<>());
        }

        // Ajouter l'objet Transport et non son ID
        event.getTransports().add(transport);

        // Sauvegarder l'événement mis à jour
        return eventRepository.save(event);
    }
}
