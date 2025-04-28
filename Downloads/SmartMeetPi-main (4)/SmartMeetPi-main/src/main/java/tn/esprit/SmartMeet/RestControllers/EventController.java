package tn.esprit.SmartMeet.RestControllers;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import tn.esprit.SmartMeet.DAO.Entities.Contract;
import tn.esprit.SmartMeet.DAO.Repositories.ContractRepository;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.DAO.Repositories.SponsorshipRequestRepository;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Entities.SponsorshipRequest;

import com.itextpdf.layout.element.Paragraph;

import org.springframework.http.ResponseEntity;
import tn.esprit.SmartMeet.Services.serviceIslem.ContractService;
import tn.esprit.SmartMeet.Services.serviceIslem.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private SponsorshipRequestRepository sponsorshipRequestRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ContractRepository contractRepository;
    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable String id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        if (event.getName() == null || event.getDescription() == null || event.getDate() == null || event.getBudget() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Event savedEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @PutMapping("/update/{id}")
    public Event updateEvent(@PathVariable String id, @RequestBody Event event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

    @PutMapping("/affecter-sponsoring/{eventId}/{offerId}")
    public ResponseEntity<String> affecterSponsoringToEvent(
            @PathVariable String eventId,
            @PathVariable String offerId) {
        eventService.addSponsoringToEvent(eventId, offerId);
        contractService.checkAndGenerateContract(eventId, offerId); // Appel après la transaction
        return ResponseEntity.ok("Offre de sponsoring associée et contrat généré !");
    }



    @PutMapping("/affecter-sponsor-request/{eventId}/{requestId}")
    public ResponseEntity<String> affecterSponsorRequest(@PathVariable String eventId, @PathVariable String requestId) {
        Optional<Event> eventOpt = eventRepo.findById(eventId);
        Optional<SponsorshipRequest> requestOpt = sponsorshipRequestRepository.findById(requestId);

        if (eventOpt.isPresent() && requestOpt.isPresent()) {
            Event event = eventOpt.get();
            SponsorshipRequest request = requestOpt.get();

            // Récupérer la liste des demandes de sponsoring associées à l'événement
            List<SponsorshipRequest> sponsorRequests = event.getSponsorRequests();
            if (sponsorRequests == null) {
                sponsorRequests = new ArrayList<>(); // Initialiser la liste si elle est null
            }

            // Vérifier si la demande est déjà associée à l'événement
            if (!sponsorRequests.contains(request)) {
                sponsorRequests.add(request); // Ajouter la demande si elle n'est pas déjà dans la liste
                event.setSponsorRequests(sponsorRequests); // Mettre à jour la liste
                eventRepo.save(event); // Sauvegarder l'événement avec la demande ajoutée
                return ResponseEntity.ok("Demande de sponsoring affectée à l'événement !");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La demande de sponsoring est déjà affectée à cet événement.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Événement ou Demande de sponsoring non trouvé.");
    }

    // Dans EventController.java
    @PostMapping("/force-generate-contract/{eventId}/{offerId}")
    public ResponseEntity<String> forceGenerateContract(
            @PathVariable String eventId,
            @PathVariable String offerId) {

        contractService.checkAndGenerateContract(eventId, offerId);
        return ResponseEntity.ok("Contrat généré manuellement");
    }
}