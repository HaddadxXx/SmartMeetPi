package tn.esprit.SmartMeet.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.Services.ISessionService;
import tn.esprit.SmartMeet.models.Session;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("sessions")
public class SessionController {


    private final ISessionService iSessionService ;

    public SessionController(ISessionService iSessionService) {
        this.iSessionService = iSessionService;
    }

    @DeleteMapping("/{id}")
    public void deleteSessions(@PathVariable String id) {
        iSessionService.deleteSessions(id);
    }

    @GetMapping("/getAllSessions")
    public List<Session> getAllSessions() {
        return iSessionService.getAllSessions();
    }

    @PutMapping("/{id}")
    public Session updateSession(@PathVariable String id,@RequestBody Session session) {
        return iSessionService.updateSession(id,session);


    }
}
