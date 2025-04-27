package tn.esprit.SmartMeet.Services.serviceIslem;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.SmartMeet.DAO.Entities.Contract;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Entities.SponsoringOffer;
import tn.esprit.SmartMeet.DAO.Repositories.ContractRepository;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.DAO.Repositories.SponsoringOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SponsoringOfferRepository sponsoringOfferRepository;

    @Autowired
    private ContractService contractService;

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

    @Transactional
    public Event addSponsoringToEvent(String eventId, String offerId) {
        // Récupérer les entités
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        SponsoringOffer offer = sponsoringOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        // Mise à jour BIDIRECTIONNELLE
        event.setSponsoringOfferId(offerId);

        eventRepository.save(event);


        contractService.checkAndGenerateContract(eventId, offerId);

        return event;
    }

}