package tn.esprit.SmartMeet.RestControllers;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Entities.MeetingRequest;
import tn.esprit.SmartMeet.DAO.Entities.Participate;
import tn.esprit.SmartMeet.DAO.Entities.Session;
import tn.esprit.SmartMeet.Services.Event.GoogleCalendarService;
import tn.esprit.SmartMeet.Services.Event.IEventService;
import tn.esprit.SmartMeet.Services.UserServices.IUserService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/events")

public class EventController {

    private final IEventService iEventService ;
private IUserService iUserService;

    public EventController(IEventService iEventService, GoogleCalendarService googleCalendarService) {
        this.iEventService = iEventService;
        this.googleCalendarService = googleCalendarService;
    }

    @PostMapping("/addEvenement")
    public ResponseEntity<?> addEvenement (@RequestPart("event") Event event,
                                           @RequestPart("file") MultipartFile file ) {
        Event savedEvent = iEventService.addEvenement(event, file );
        System.out.println("ownerid "+ event.getOwnerId() + "event creeé"+event.getNomEvent());
        return ResponseEntity.ok(savedEvent);
    }
  /* @PostMapping("/addEvenement")
   public ResponseEntity<?> addEvenement(
           @RequestPart("event") Event event,
           @RequestPart("file") MultipartFile file,
           @RequestParam("userId") String userId) {

       Event savedEvent = iEventService.addEvenement(event, file, userId);
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
    public Event updateEvent(@PathVariable String id,@RequestBody Event event){
        System.out.println("Event reçu: " + event);  // ✅ Debug ici
        System.out.println("Capacité reçue: " + event.getCapacite()); // ✅ Debug ici
        return iEventService.updateEvent(id,event);
    }

    @PostMapping("/ajouterSessionEtAffecterAEvenement/{eventName}")
    public Session ajouterSessionEtAffecterAEvenement(@RequestBody Session session, @PathVariable String eventName){
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

  /*  @GetMapping("/getEventsByOwner/{ownerId}")
    public List<Event> getEventsByOwner( String ownerId){
       return iEventService.getEventsByOwner(ownerId);
    }*/



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

  /*  @PostMapping("/createMeetEvent")
    public String createMeetEvent(@RequestParam String summary,
                                  @RequestParam String description,
                                  @RequestParam String startDateTime,
                                  @RequestParam String endDateTime,
                                  @RequestParam String timeZone) {
        try {
            // Appel de la méthode pour créer l'événement avec le lien Meet
            return googleCalendarService.createEventWithMeetLink(summary, description, startDateTime, endDateTime, timeZone);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la création de l'événement : " + e.getMessage();
        }
    }*/

/*    @PostMapping("/create")
    public ResponseEntity<String>createEventWithMeet(@RequestBody MeetingRequest request) {
        try {
            String meetLink = googleCalendarService.createEventWithMeet(
                    request.getTitle(),
                    request.getDescription(),
                    request.getStartDateTime(),
                    request.getEndDateTime()
            );
            return ResponseEntity.ok(meetLink);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création : " + e.getMessage());
        }
    }*/
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


  /*  @PostMapping("/participate")
    public ResponseEntity<?> participate(
            @RequestParam String email,
            @RequestParam String eventId) {

        Participate result = iEventService.participateToEvent(email, eventId);

        // ✅ On récupère l'utilisateur depuis l'objet result
        if (result.getUser() != null) {
            System.out.println("User ID: " + result.getUser().getId());
        } else {
            System.out.println("User is null.");
        }

        return ResponseEntity.ok(result);
    }*/
 @PostMapping(value = "/participateToEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> participateToEvent(
          @RequestParam String email,
          @RequestParam String eventId,
          @RequestPart(required = false) MultipartFile file) {

      Participate result = iEventService.participateToEvent(email, eventId, file);
      return ResponseEntity.ok(result);
  }

    @GetMapping("/ afficherParticipantsParCreateur/{userId}")
    public void afficherParticipantsParCreateur(@PathVariable String userId) {
        iEventService.afficherParticipantsParCreateur(userId);
    }




    @PostMapping("/createEventWithMeetLink")
    public ResponseEntity<Event> createEventWithMeetLink(@RequestBody MeetingRequest meetingRequest,
                                                         @RequestPart("file") MultipartFile file ) throws Exception {

        Event event = iEventService.createEventWithMeetLink(meetingRequest, file);
        return new ResponseEntity<>(event, HttpStatus.CREATED);

    }




    @PostMapping("/lancerMeetPourEvent/{eventId}")
    public ResponseEntity<Map<String, String>> lancerMeetPourEvent(@PathVariable String eventId) {
        // Création du lien Google Meet pour l'événement
        String meetLink = iEventService.lancerMeetPourEvent(eventId);

        // Création de la réponse JSON
        Map<String, String> response = new HashMap<>();
        response.put("link", meetLink);  // Ajouter le lien dans le JSON

        // Retourne un objet JSON avec le lien de la réunion
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getParticipantsEmails/{eventId}")
    public ResponseEntity<List<String>> getParticipantsEmails(@PathVariable String eventId) {
        try {
            List<String> emails = iEventService.getParticipantsEmails(eventId);
            return ResponseEntity.ok(emails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
