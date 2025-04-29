package tn.esprit.SmartMeet.Services.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.DAO.Repositories.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class SessionService implements ISessionService  {
    @Autowired
    private SessionRepo sessionRepository;
    @Autowired
    private RessourceRepo ressourceRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(String id) {
        return sessionRepository.findById(id);
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session updateSession(String id, Session updatedSession) { updatedSession.setIdSession(id);
        return sessionRepository.save(updatedSession);
    }

    public void deleteSession(String id) {sessionRepository.deleteById(id);}

    @Override
    public String assignRessourceToSession(String sessionId, String ressourceId) {
        Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
        Optional<Ressource> ressourceOpt = ressourceRepository.findById(ressourceId);

        if (sessionOpt.isPresent() && ressourceOpt.isPresent()) {
            Session session = sessionOpt.get();
            Ressource ressource = ressourceOpt.get();

            List<Ressource> ressources = session.getRessources();
            if (ressources == null) {
                ressources = new ArrayList<>();
            }

            if (!ressources.contains(ressource)) {
                ressources.add(ressource);
                session.setRessources(ressources);
                sessionRepository.save(session);
                return "OK";
            } else {
                return "ALREADY_ASSIGNED";
            }
        }

        return "NOT_FOUND";
    }


}