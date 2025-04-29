/*package tn.esprit.SmartMeet.Services.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Repositories.*;
import tn.esprit.SmartMeet.DAO.Entities.*;

import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {
    @Autowired
    private EventRepo eventRepository;
    @Autowired
    private  TransportRepo transportRepository;
    @Autowired
    private TransportAssignmentRepo transportAssignmentRepo;


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
        // Delete associated assignments
        List<TransportAssignment> assignments = transportAssignmentRepo.findAll().stream()
                .filter(assignment -> assignment.getEvent().getId().equals(id))
                .toList();
        transportAssignmentRepo.deleteAll(assignments);
        eventRepository.deleteById(id);
    }

    @Override
    public String assignTransportToEvent(String eventId, String transportId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<Transport> transportOpt = transportRepository.findById(transportId);

        if (eventOpt.isEmpty() || transportOpt.isEmpty()) {
            return "NOT_FOUND";
        }

        Event event = eventOpt.get();
        Transport transport = transportOpt.get();

        // Check if the transport is already assigned to this event
        boolean alreadyAssigned = transportAssignmentRepo.findByTransportId(transportId).stream()
                .anyMatch(assignment -> assignment.getEvent().getId().equals(eventId));
        if (alreadyAssigned) {
            return "ALREADY_ASSIGNED";
        }

        // Check availability on the event's date
        List<TransportAssignment> conflictingAssignments = transportAssignmentRepo.findByTransportIdAndAssignmentDate(
                transportId, event.getDateE());
        if (!conflictingAssignments.isEmpty()) {
            return "NOT_AVAILABLE";
        }

        // Create a new TransportAssignment
        TransportAssignment assignment = new TransportAssignment();
        assignment.setTransport(transport);
        assignment.setEvent(event);
        assignment.setAssignmentDate(event.getDateE());
        transportAssignmentRepo.save(assignment);
        // Update the transport's status to "Reserved"
        transport.setStatut("Reserved");
        transportRepository.save(transport);
        return "OK";
    }

}*/
package tn.esprit.SmartMeet.Services.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Entities.Transport;
import tn.esprit.SmartMeet.DAO.Entities.TransportAssignment;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepo;
import tn.esprit.SmartMeet.DAO.Repositories.TransportAssignmentRepo;
import tn.esprit.SmartMeet.DAO.Repositories.TransportRepo;

import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {

    @Autowired
    private EventRepo eventRepository;

    @Autowired
    private TransportRepo transportRepository;

    @Autowired
    private TransportAssignmentRepo transportAssignmentRepo;

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
        updatedEvent.setIdEvent(id);
        return eventRepository.save(updatedEvent);
    }

    public void deleteEvent(String id) {
        List<TransportAssignment> assignments = transportAssignmentRepo.findAll().stream()
                .filter(assignment -> assignment.getEvent().getIdEvent().equals(id))
                .toList();
        transportAssignmentRepo.deleteAll(assignments);
        eventRepository.deleteById(id);
    }

    @Override
    public String assignTransportToEvent(String eventId, String transportId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        Optional<Transport> transportOpt = transportRepository.findById(transportId);

        if (eventOpt.isEmpty() || transportOpt.isEmpty()) {
            return "NOT_FOUND";
        }

        Event event = eventOpt.get();
        Transport transport = transportOpt.get();

        boolean alreadyAssigned = transportAssignmentRepo.findByTransportId(transportId).stream()
                .anyMatch(assignment -> assignment.getEvent().getIdEvent().equals(eventId));
        if (alreadyAssigned) {
            return "ALREADY_ASSIGNED";
        }

        // Check for overlapping assignments
        List<TransportAssignment> conflictingAssignments = transportAssignmentRepo.findByTransportIdAndDateRange(
                transportId, event.getDateDebut(), event.getDateFin());
        if (!conflictingAssignments.isEmpty()) {
            return "NOT_AVAILABLE";
        }

        TransportAssignment assignment = new TransportAssignment();
        assignment.setTransport(transport);
        assignment.setEvent(event);
        assignment.setDateDebut(event.getDateDebut());
        assignment.setDateFin(event.getDateFin());
        transportAssignmentRepo.save(assignment);

        // Update the transport's status to "Reserved"
        transport.setStatut("Reserved");
        transportRepository.save(transport);

        return "OK";
    }
}