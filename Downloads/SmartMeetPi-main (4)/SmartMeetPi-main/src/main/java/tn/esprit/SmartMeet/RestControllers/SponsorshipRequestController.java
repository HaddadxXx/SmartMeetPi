package tn.esprit.SmartMeet.RestControllers;
import tn.esprit.SmartMeet.DAO.Entities.RequestStatus;
import tn.esprit.SmartMeet.DAO.Entities.SponsorshipRequest;
import tn.esprit.SmartMeet.Services.serviceIslem.SponsorshipRequestService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
@RestController
@RequestMapping("/api/requests")
public class SponsorshipRequestController {
    @Autowired
    private SponsorshipRequestService service;


    // 🔹 Ajouter une nouvelle demande
    @PostMapping("/create")
    public SponsorshipRequest createRequest(@RequestBody SponsorshipRequest request) {
        return service.createRequest(request);
    }

    // 🔹 Récupérer toutes les demandes
    @GetMapping("/all")
    public List<SponsorshipRequest> getAllRequests() {
        return service.getAllRequests();
    }

    // 🔹 Récupérer une demande par ID
    @GetMapping("/{id}")
    public Optional<SponsorshipRequest> getRequestById(@PathVariable String id) {
        return service.getRequestById(id);
    }

    // 🔹 Mettre à jour une demande
    @PutMapping("/update/{id}")
    public SponsorshipRequest updateRequest(@PathVariable String id, @RequestBody SponsorshipRequest request) {
        return service.updateRequest(id, request);
    }

    // 🔹 Supprimer une demande
    @DeleteMapping("/delete/{id}")
    public void deleteRequest(@PathVariable String id) {
        service.deleteRequest(id);
    }
}
