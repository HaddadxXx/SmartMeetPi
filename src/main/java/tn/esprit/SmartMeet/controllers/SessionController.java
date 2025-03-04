package tn.esprit.SmartMeet.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.Repositories.*;
import tn.esprit.SmartMeet.models.*;
import tn.esprit.SmartMeet.services.SessionService;

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
    private SessionService sessionService;

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
        Optional<Session> sessionOpt = SessionRepo.findById(sessionId);
        Optional<Ressource> ressourceOpt = RessourceRepo.findById(ressourceId);
        if (sessionOpt.isPresent() && ressourceOpt.isPresent()) {
            Session session = sessionOpt.get();
            Ressource ressource = ressourceOpt.get();

            // Récupérer la liste des transports existants de l'événement
            List<Ressource> ressources = session.getRessources();
            if (ressources == null) {
                ressources = new ArrayList<>(); // Initialiser la liste si elle est null
            }

            // Vérifier si le transport est déjà présent dans la liste
            if (!ressources.contains(ressource)) {
                ressources.add(ressource); // Ajouter le transport uniquement s'il n'est pas déjà dans la liste
                session.setRessources(ressources); // Mettre à jour la liste des transports de l'événement
                SessionRepo.save(session); // Sauvegarder l'événement avec le transport ajouté
                return ResponseEntity.ok("Resource assigned to the event!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The resource is already assigned to this session");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session or Resource not found");
    }
}
