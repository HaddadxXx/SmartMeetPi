package tn.esprit.SmartMeet.Services;

import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.Repositories.SessionRepository;
import tn.esprit.SmartMeet.models.Session;

import java.util.List;
@Service
public class SessionService implements ISessionService{

    private final SessionRepository sessionRepository ;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void deleteSessions(String idSession) {
        sessionRepository.deleteById(idSession);

    }

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
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
