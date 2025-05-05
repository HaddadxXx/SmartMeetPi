package tn.esprit.SmartMeet.Services.Event;

import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.Session;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.DAO.Repositories.SessionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService implements ISessionService {

    private final SessionRepository sessionRepository ;
    private final EventRepository eventRepository ;

    public SessionService(SessionRepository sessionRepository, EventRepository eventRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public Session addSession(Session session){
        return sessionRepository.save(session);
    }


    @Override
    public void deleteSessions(String id) {
        sessionRepository.deleteById(id);
    }

    @Override
    public List<Session> getAllSessions() {
        List<Session> sessions = sessionRepository.findAll();

        for (Session session : sessions) {
            String eventName = (session.getEvenement() != null)
                    ? session.getEvenement().getNomEvent()
                    : "Non associé";
            session.setNomEvent(eventName);
        }

        return sessions;
    }
    @Override
    public Session getSessionById(String id) {
        return sessionRepository.findById(id).orElse(null);
    }

    @Override
    public Session updateSession(String id, Session session) {
        Session existingSession = sessionRepository.findById(id).orElse(null);

        if (existingSession != null) {
            // Mettre à jour les champs non nulls
            if (session.getTitre() != null) existingSession.setTitre(session.getTitre());
            if (session.getDate() != null) existingSession.setDate(session.getDate());

            return sessionRepository.save(existingSession);  // Sauvegarde la session mise à jour
        } else {
            throw new RuntimeException("Session not found with ID: " + id);
        }
    }
}
