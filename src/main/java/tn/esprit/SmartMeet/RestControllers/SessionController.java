/*package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Repositories.*;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.Services.Session.ISessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:4200")
public class SessionController {
    @Autowired
    private SessionRepo SessionRepo;
    @Autowired
    private RessourceRepo RessourceRepo;
    @Autowired
    private ISessionService sessionService;

    @GetMapping("/all")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public Optional<Session> getSessionById(@PathVariable String id) {
        return sessionService.getSessionById(id);
    }

    @PostMapping("/create")
    public Session createSession(@RequestBody Session session) {
        return sessionService.createSession(session);
    }

    @PutMapping("/update/{id}")
    public Session updateSession(@PathVariable String id, @RequestBody Session session) {
        return sessionService.updateSession(id, session);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSession(@PathVariable String id) {
        sessionService.deleteSession(id);
    }

   @PutMapping("/affecter-ressource/{sessionId}/{ressourceId}")
   public ResponseEntity<String> affecterRessource(@PathVariable String sessionId, @PathVariable String ressourceId) {
       String result = sessionService.assignRessourceToSession(sessionId, ressourceId);

       switch (result) {
           case "OK":
               return ResponseEntity.ok("Resource assigned to the session!");
           case "ALREADY_ASSIGNED":
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The resource is already assigned to this session.");
           case "NOT_FOUND":
           default:
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session or Resource not found.");
       }
   }
}*/
package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Repositories.*;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.Services.Session.ISessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:4200")
public class SessionController {
    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private RessourceRepo ressourceRepo;

    @Autowired
    private ISessionService sessionService;

    @GetMapping("/all")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public Optional<Session> getSessionById(@PathVariable String id) {
        return sessionService.getSessionById(id);
    }

    @PostMapping("/create")
    public Session createSession(@RequestBody Session session) {
        return sessionService.createSession(session);
    }

    @PutMapping("/update/{id}")
    public Session updateSession(@PathVariable String id, @RequestBody Session session) {
        return sessionService.updateSession(id, session);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSession(@PathVariable String id) {
        sessionService.deleteSession(id);
    }

    @PutMapping("/affecter-ressource/{sessionId}/{ressourceId}")
    public ResponseEntity<String> affecterRessource(@PathVariable String sessionId, @PathVariable String ressourceId) {
        String result = sessionService.assignRessourceToSession(sessionId, ressourceId);

        switch (result) {
            case "OK":
                return ResponseEntity.ok("Resource assigned to the session!");
            case "ALREADY_ASSIGNED":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The resource is already assigned to this session.");
            case "NOT_FOUND":
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session or Resource not found.");
        }
    }

    // Endpoint to fetch sessions by resource ID
    @GetMapping("/by-ressource/{ressourceId}")
    public ResponseEntity<List<Session>> getSessionsByRessourceId(@PathVariable String ressourceId) {
        List<Session> sessions = sessionService.getSessionsByRessourceId(ressourceId);
        return ResponseEntity.ok(sessions);
    }
}
