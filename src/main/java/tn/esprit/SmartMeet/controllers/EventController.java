package tn.esprit.SmartMeet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.Repositories.EventRepo;
import tn.esprit.SmartMeet.Repositories.TransportRepo;
import tn.esprit.SmartMeet.models.*;
import tn.esprit.SmartMeet.services.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {


    @Autowired
    private EventRepo EventRepo;
    @Autowired
    private TransportRepo TransportRepo;
    @Autowired
    private EventService eventService;

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/create")
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }


    @PutMapping("/affecter-transport/{eventId}/{transportId}")
    public ResponseEntity<String> affecterTransport(@PathVariable String eventId, @PathVariable String transportId) {
        Optional<Event> eventOpt = EventRepo.findById(eventId);
        Optional<Transport> transportOpt = TransportRepo.findById(transportId);
        if (eventOpt.isPresent() && transportOpt.isPresent()) {
            Event event = eventOpt.get();
            Transport transport = transportOpt.get();

            // Récupérer la liste des transports existants de l'événement
            List<Transport> transports = event.getTransports();
            if (transports == null) {
                transports = new ArrayList<>(); // Initialiser la liste si elle est null
            }

            // Vérifier si le transport est déjà présent dans la liste
            if (!transports.contains(transport)) {
                transports.add(transport); // Ajouter le transport uniquement s'il n'est pas déjà dans la liste
                event.setTransports(transports); // Mettre à jour la liste des transports de l'événement
                EventRepo.save(event); // Sauvegarder l'événement avec le transport ajouté
                return ResponseEntity.ok("Transport assigned to the event!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The transport is already assigned to this event.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event or Transport not found.");
    }




}
