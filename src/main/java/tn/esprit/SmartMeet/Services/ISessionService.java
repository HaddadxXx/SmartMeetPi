package tn.esprit.SmartMeet.Services;

import tn.esprit.SmartMeet.models.Session;

import java.util.List;

public interface ISessionService {

    void deleteSessions(String id) ;
    List<Session> getAllSessions() ;
    Session updateSession(String id, Session session);
}
