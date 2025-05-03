package tn.esprit.SmartMeet.RestControllers;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.Services.Event.EventService;
import tn.esprit.SmartMeet.Services.Event.GoogleCalendarService;
import tn.esprit.SmartMeet.Services.Event.IEventService;
import tn.esprit.SmartMeet.Services.UserServices.IUserService;
import tn.esprit.SmartMeet.Services.UserServices.RahmaMailService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/events")

public class EventController {
    @Autowired
    private EventService eventService;

    private final IEventService iEventService;
    private IUserService iUserService;

    public EventController(IEventService iEventService, GoogleCalendarService googleCalendarService) {
        this.iEventService = iEventService;
        this.googleCalendarService = googleCalendarService;
    }

   @PostMapping("/addEvenement")
    public ResponseEntity<?> addEvenement(@RequestPart("event") Event event,
                                          @RequestPart("file") MultipartFile file) {
        Event savedEvent = iEventService.addEvenement(event, file);
        System.out.println("ownerid " + event.getOwnerId() + "event creeé" + event.getNomEvent());
        return ResponseEntity.ok(savedEvent);
    }
  /*@PostMapping(value = "/addEvenement", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Event> addEvenement(
          @RequestPart("evenement") Event evenement,
          @RequestPart("file") MultipartFile file) {
      Event savedEvent = eventService.addEvenement(evenement, file);
      return ResponseEntity.ok(savedEvent);
  }*/


    @GetMapping("/getAllEvents")
    public List<Event> getAllEvents() {
        return iEventService.getAllEvents();
    }


    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable String id) {
        iEventService.deleteEvent(id);
    }


    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event event) {
        System.out.println("Event reçu: " + event);  // ✅ Debug ici
        System.out.println("Capacité reçue: " + event.getCapacite()); // ✅ Debug ici
        return iEventService.updateEvent(id, event);
    }

    @PostMapping("/ajouterSessionEtAffecterAEvenement/{eventName}")
    public Session ajouterSessionEtAffecterAEvenement(@RequestBody Session session, @PathVariable String eventName) {
        System.out.println("Nom de l'événement reçu : " + eventName);
        return iEventService.ajouterSessionEtAffecterAEvenement(session, eventName);
    }

    private final Path uploadDir = Paths.get("uploads");

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Déterminer le type MIME dynamiquement
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    private final GoogleCalendarService googleCalendarService;


    @GetMapping("/test")
    public ResponseEntity<String> testGoogleCalendarConnection() {
        try {
            // Juste pour tester que tu récupères bien le service
            Calendar calendar = googleCalendarService.getCalendarService();

            // Exemple simple : récupérer les 10 prochains événements
            Events events = calendar.events().list("primary")
                    .setMaxResults(10)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            StringBuilder response = new StringBuilder("Upcoming events:\n");
            for (com.google.api.services.calendar.model.Event event : events.getItems()) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                response.append(event.getSummary()).append(" at ").append(start).append("\n");
            }

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur Google Calendar: " + e.getMessage());
        }
    }


    @PostMapping("/create")
    public ResponseEntity<String> createEventWithMeet(@RequestBody MeetingRequest request) {
        try {
            String meetLink = googleCalendarService.createEventWithMeet(request);
            return ResponseEntity.ok(meetLink);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping("/callback")
    public String handleGoogleCallback() {
        // Ici, tu peux gérer la logique post-auth, ou simplement indiquer que l'autorisation a réussi
        return "Autorisation réussie ! Vous pouvez fermer cette fenêtre.";
    }

    @GetMapping("/paginated")
    public Page<Event> getPaginatedEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return iEventService.getAllEvents(pageable);
    }


    @PostMapping(value = "/participateToEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> participateToEvent(
            @RequestParam String email,
            @RequestParam String eventId,
            @RequestPart(required = false) MultipartFile file) {

        Participate result = iEventService.participateToEvent(email, eventId, file);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/createEventWithMeetLink")
    public ResponseEntity<Event> createEventWithMeetLink(@RequestBody MeetingRequest meetingRequest,
                                                         @RequestPart("file") MultipartFile file) throws Exception {

        Event event = iEventService.createEventWithMeetLink(meetingRequest, file);
        return new ResponseEntity<>(event, HttpStatus.CREATED);

    }


    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analyzeEventTheme(@RequestParam("file") MultipartFile file,
                                               @RequestParam("theme") String theme) throws IOException {
        // Sauvegarder temporairement le fichier
        File tempFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(tempFile);

        Map<String, Object> result = eventService.analyzeFileWithAI(tempFile, theme);

        // Supprimer le fichier temporaire après analyse
        tempFile.delete();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/participants-emails/{eventId}")
    public List<String> getParticipantsEmailsByEventId(@PathVariable String eventId) {
        return iEventService.getParticipantsEmailsByEventId(eventId);
    }


    @PostMapping("/lancerMeetPourEvent/{eventId}")
    public ResponseEntity<Map<String, String>> lancerMeetPourEvent(@PathVariable String eventId) {
        String meetLink = eventService.lancerMeetPourEvent(eventId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Lien Google Meet généré");
        response.put("meetLink", meetLink);

        return ResponseEntity.ok(response);
    }


 /*   @GetMapping("/evenement-tendance")
    public ResponseEntity<Event> getEvenementTendance() {
        return ResponseEntity.ok(eventService.getEvenementTendance());
    }*/

    @PostMapping("/verifier-etat/{eventId}")
    public ResponseEntity<String> verifierEtatEvenement(@PathVariable String eventId) {
        try {
            iEventService.verifierEtatEvenement(eventId);
            return ResponseEntity.ok("Événement vérifié avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

  /*  @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        try {
            User user = iEventService.getCurrentUser(principal.getName());
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }*/
  @GetMapping("/events/tendance")
  public ResponseEntity<List<Event>> getTop5EvenementsTendance() {
      List<Event> topEvents = eventService.getTop5EvenementsTendance();
      return ResponseEntity.ok(topEvents);
  }
}
