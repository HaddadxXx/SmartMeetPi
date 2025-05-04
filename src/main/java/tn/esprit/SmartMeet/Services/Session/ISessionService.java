package tn.esprit.SmartMeet.Services.Session;
import tn.esprit.SmartMeet.DAO.Entities.Session;

import java.util.List;
import java.util.Optional;

public interface ISessionService {
    List<Session> getAllSessions();
    Optional<Session> getSessionById(String id);
    Session createSession(Session session);
    Session updateSession(String id, Session updatedSession);
    void deleteSession(String id);
    String assignRessourceToSession(String sessionId, String ressourceId);
    List<Session> getSessionsByRessourceId(String ressourceId);
}

