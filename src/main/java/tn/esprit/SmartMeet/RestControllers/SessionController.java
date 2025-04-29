package tn.esprit.SmartMeet.RestControllers;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Session;
import tn.esprit.SmartMeet.Services.Event.ISessionService;

import java.util.List;

@RequestMapping("/Session")
@CrossOrigin
@Service
public class SessionController {

    private final ISessionService iSessionService ;


    public SessionController(ISessionService iSessionService) {
        this.iSessionService = iSessionService;
    }


    @PostMapping("/addSession")
    public Session addSession(@RequestBody Session session){
        return iSessionService.addSession(session);
    }

    @DeleteMapping("/{id}")
    public void deleteSessions(@PathVariable String id) {
        iSessionService.deleteSessions(id);
    }

    @GetMapping("/getAllSessions")
    public List<Session> getAllSessions() {
        return iSessionService.getAllSessions();
    }

    @GetMapping("/getSessionById")
    public Session getSessionById(@PathVariable String id) {
        return iSessionService.getSessionById(id);
    }

    @PutMapping("/{id}")
    public Session updateSession(@PathVariable String id,@RequestBody Session session) {
        return iSessionService.updateSession(id,session);


    }
}
