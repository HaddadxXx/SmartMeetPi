package tn.esprit.SmartMeet.Services.serviceIslem;

import tn.esprit.SmartMeet.DAO.Repositories.SponsorshipRequestRepository;
import tn.esprit.SmartMeet.DAO.Entities.RequestStatus;
import tn.esprit.SmartMeet.DAO.Entities.SponsorshipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SponsorshipRequestService {

    @Autowired
    private SponsorshipRequestRepository repository;

    // Créer une demande de sponsoring avec contrôle de saisie
    public SponsorshipRequest createRequest(SponsorshipRequest request) {
        // Vérifier que les champs obligatoires ne sont pas vides
        if (request.getEventDescription() == null || request.getEventDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description de l'événement ne peut pas être vide.");
        }
        if (request.getStatusR() == null) {
            throw new IllegalArgumentException("Le statut ne peut pas être vide.");
        }

        // Vérifier que la date de la demande est aujourd'hui ou dans le futur
        if (request.getRequestDate() == null) {
            throw new IllegalArgumentException("La date de la demande ne peut pas être vide.");
        }

        LocalDate today = LocalDate.now();
        LocalDate requestDate = request.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (requestDate.isBefore(today)) {
            throw new IllegalArgumentException("La date de la demande doit être aujourd'hui ou dans le futur.");
        }

        // Fixer la date de la demande à aujourd'hui si elle est fournie vide
        if (request.getRequestDate() == null) {
            request.setRequestDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        return repository.save(request);
    }

    // Lire toutes les demandes
    public List<SponsorshipRequest> getAllRequests() {
        return repository.findAll();
    }

    // Lire une demande par ID
    public Optional<SponsorshipRequest> getRequestById(String id) {
        return repository.findById(id);
    }

    // Mettre à jour une demande
    public SponsorshipRequest updateRequest(String id, SponsorshipRequest updatedRequest) {
        return repository.findById(id).map(request -> {
            // Vérifier que les champs obligatoires ne sont pas vides
            if (updatedRequest.getEventDescription() == null || updatedRequest.getEventDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("La description de l'événement ne peut pas être vide.");
            }
            if (updatedRequest.getStatusR() == null) {
                throw new IllegalArgumentException("Le statut ne peut pas être vide.");
            }

            // Vérifier que la date de la demande est aujourd'hui ou dans le futur
            LocalDate today = LocalDate.now();
            LocalDate requestDate = updatedRequest.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (requestDate.isBefore(today)) {
                throw new IllegalArgumentException("La date de la demande doit être aujourd'hui ou dans le futur.");
            }

            request.setEventDescription(updatedRequest.getEventDescription());
            request.setStatus(updatedRequest.getStatusR());
            request.setRequestDate(updatedRequest.getRequestDate());

            return repository.save(request);
        }).orElse(null);
    }

    // Supprimer une demande
    public void deleteRequest(String id) {
        repository.deleteById(id);
    }
}
