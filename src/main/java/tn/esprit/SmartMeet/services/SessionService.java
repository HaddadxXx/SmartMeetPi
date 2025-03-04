package tn.esprit.SmartMeet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.Repositories.RessourceRepo;
import tn.esprit.SmartMeet.Repositories.SessionRepo;
import tn.esprit.SmartMeet.models.Ressource;
import tn.esprit.SmartMeet.models.Session;

import java.util.List;
import java.util.Optional;
@Service
public class SessionService {
    @Autowired
    private SessionRepo sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(String id) {
        return sessionRepository.findById(id);
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session updateSession(String id, Session updatedSession) { updatedSession.setId(id);
        return sessionRepository.save(updatedSession);
    }

    public void deleteSession(String id) {
        sessionRepository.deleteById(id);
    }
}


