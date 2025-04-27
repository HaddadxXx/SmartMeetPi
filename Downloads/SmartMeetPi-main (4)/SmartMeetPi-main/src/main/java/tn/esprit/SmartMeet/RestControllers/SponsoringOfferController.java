package tn.esprit.SmartMeet.RestControllers;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Entities.SponsoringOffer;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.DAO.Repositories.SponsoringOfferRepository;
import tn.esprit.SmartMeet.Services.serviceIslem.ContractService;
import tn.esprit.SmartMeet.Services.serviceIslem.SponsoringOfferService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@AllArgsConstructor
@RequestMapping("/api/offers")
public class SponsoringOfferController {
    @Autowired
    private SponsoringOfferService service;
    @Autowired
    private ContractService contractService;
    @Autowired
    private SponsoringOfferRepository sponsoringOfferRepository;



    @GetMapping
    public List<SponsoringOffer> getAllOffers() {
        List<SponsoringOffer> offers = service.getAllOffers();
        System.out.println("Offres trouvées dans la base de données: " + offers);
        return offers;
    }
    @PostMapping
    public SponsoringOffer createOffer(@RequestBody SponsoringOffer offer) {
        System.out.println("Offre reçue : " + offer);  // Ajoute ce log
        try {
            return service.createOffer(offer);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public SponsoringOffer updateOffer(@PathVariable String id, @RequestBody SponsoringOffer updatedOffer) {
        try {
            return service.updateOffer(id, updatedOffer);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOffer(@PathVariable String id) {
        service.deleteOffer(id);
    }
    @PutMapping("/affecter-event-a-offre/{offerId}/{eventId}")
    public ResponseEntity<String> affecterEventToSponsoringOffer(
            @PathVariable String offerId,
            @PathVariable String eventId) {
        service.addEventToSponsoringOffer(offerId, eventId);
        contractService.checkAndGenerateContract(eventId, offerId); // Appel après la transaction
        return ResponseEntity.ok("Événement associé à l’offre et contrat généré !");
    }
}

