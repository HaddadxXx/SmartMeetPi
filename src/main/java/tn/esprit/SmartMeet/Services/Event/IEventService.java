package tn.esprit.SmartMeet.Services.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.*;

import java.util.List;

public interface IEventService {

    //void processEventParticipation(String eventId);
    Event addEvenement(Event evenement, MultipartFile file) ;
    List<Event> getAllEvents();
    Event getEventById(String id);
    void deleteEvent(String id);
    Event updateEvent(String id, Event event);
    public Session ajouterSessionEtAffecterAEvenement(Session session, String eventName);
     void verifierEtatEvenement(String eventId);
    Page<Event> getAllEvents(Pageable pageable);

   // List<Event> getEventsByOwner( String ownerId);
    User getUserByOwnerEvent(String eventId);
    Participate participateToEvent(String email, String eventId, MultipartFile file);
   // Participate participateToEvent(String email, String eventId) ;
   public void afficherParticipantsParCreateur(String userId);
    Event createEventWithMeetLink(MeetingRequest request, MultipartFile file)throws Exception;

    public String lancerMeetPourEvent(String eventId) ;

    List<String> getParticipantsEmails(String eventId);
}
