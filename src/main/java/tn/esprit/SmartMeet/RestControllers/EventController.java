package tn.esprit.SmartMeet.RestControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.DAO.Repositories.*;

import tn.esprit.SmartMeet.Services.Event.IEventService;

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
    private IEventService eventService;

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/create")
    public Event createEvent(@Valid @RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @PutMapping("/update/{id}")
    public Event updateEvent(@Valid @PathVariable String id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

   @PutMapping("/affecter-transport/{eventId}/{transportId}")
   public ResponseEntity<String> affecterTransport(@PathVariable String eventId, @PathVariable String transportId) {
       String result = eventService.assignTransportToEvent(eventId, transportId);

       switch (result) {
           case "OK":
               return ResponseEntity.ok("Transport assigned to the event!");
           case "ALREADY_ASSIGNED":
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The transport is already assigned to this event.");
           case "NOT_AVAILABLE":
               return ResponseEntity.status(HttpStatus.CONFLICT).body("The transport is not available on this date.");
           case "NOT_FOUND":
           default:
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event or Transport not found.");
       }
   }



}
