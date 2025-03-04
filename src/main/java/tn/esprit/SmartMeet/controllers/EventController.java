package tn.esprit.SmartMeet.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.Services.IEventService;
import tn.esprit.SmartMeet.models.Event;
import tn.esprit.SmartMeet.models.Session;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/events")
public class EventController {

    private final IEventService iEventService ;


    public EventController(IEventService iEventService) {
        this.iEventService = iEventService;
    }

    @PostMapping("/addEvenement")
    public Event addEvenement(@RequestBody Event evenement){
        System.out.println("Réception de l'événement : " + evenement); // Log pour debug

        return iEventService.addEvenement(evenement);
    }

    @GetMapping("/getAllEvents")
    public List<Event> getAllEvents() {
        return iEventService.getAllEvents();
    }

    @GetMapping("/getEventById")
    public Event getEventById(@PathVariable String id) {
        return iEventService.getEventById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable String id) {
        iEventService.deleteEvent(id);
    }



    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable String id,@RequestBody Event event){
        return iEventService.updateEvent(id,event);
    }

    @PostMapping("/ajouterSessionEtAffecterAEvenement/{eventName}")
    public Session ajouterSessionEtAffecterAEvenement(@RequestBody Session session, @PathVariable String eventName){
        System.out.println("Nom de l'événement reçu : " + eventName);
        return iEventService.ajouterSessionEtAffecterAEvenement(session, eventName);
    }

}
