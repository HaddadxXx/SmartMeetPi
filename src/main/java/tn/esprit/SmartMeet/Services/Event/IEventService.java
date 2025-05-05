package tn.esprit.SmartMeet.Services.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IEventService {

    //void processEventParticipation(String eventId);
    Event addEvenement(Event evenement, MultipartFile file) ;
    List<Event> getAllEvents();
    Optional<Event> getEventById(String id);
    List<Event> getEventsByOwner( String ownerId);
    void deleteEvent(String id);
    public Event updateEvent(String id, Event event, MultipartFile file);
    // Event updateEvent(String id, Event event);
    public Session ajouterSessionEtAffecterAEvenement(Session session, String eventName);
    void verifierEtatEvenement(String eventId);
    Page<Event> getAllEvents(Pageable pageable);
    User getUserByOwnerEvent(String eventId);
    Participate participateToEvent(String email, String eventId, MultipartFile file);

    Event createEventWithMeetLink(MeetingRequest request, MultipartFile file)throws Exception;

    public String lancerMeetPourEvent(String eventId) ;

    // List<String> getParticipantsEmails(String eventId);
    public Map<String, Object> analyzeFileWithAI(File file, String theme);
    public List<String> getParticipantsEmailsByEventId(String eventId);
    //  void sendEmailToParticipants(String eventId);
    public List<Event> getTop5EvenementsTendance();

    User getOwnerByEventId(String eventId);
    Event getEventByAuthenticated(String email) ;
    List<Event> getEventByAuthenticatedUser(String email)  ;
}
