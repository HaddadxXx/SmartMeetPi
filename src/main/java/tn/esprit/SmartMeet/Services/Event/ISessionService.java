package tn.esprit.SmartMeet.Services.Event;

import tn.esprit.SmartMeet.DAO.Entities.Session;

import java.util.List;

public interface ISessionService {

    Session addSession(Session session);
    // Session addSessions (Session sessions);
    void deleteSessions(String id) ;
    List<Session> getAllSessions() ;
    Session getSessionById(String id) ;
    Session updateSession(String id, Session session);
}
