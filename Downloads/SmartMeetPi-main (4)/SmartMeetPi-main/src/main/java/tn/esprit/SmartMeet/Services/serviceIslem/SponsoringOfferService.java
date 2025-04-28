package tn.esprit.SmartMeet.Services.serviceIslem;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.SmartMeet.DAO.Entities.Contract;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Repositories.ContractRepository;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.DAO.Repositories.SponsoringOfferRepository;
import tn.esprit.SmartMeet.DAO.Entities.OfferStatus;
import tn.esprit.SmartMeet.DAO.Entities.SponsoringOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SponsoringOfferService {

    @Autowired
    private SponsoringOfferRepository sponsoringOfferRepository;
    @Autowired
    private ContractService contractService;


    @Autowired
    private EventRepository eventRepository;

    public List<SponsoringOffer> getAllOffers() {
        return sponsoringOfferRepository.findAll();
    }

    public SponsoringOffer getOfferById(String id) {
        return sponsoringOfferRepository.findById(id).orElse(null);
    }

    public SponsoringOffer createOffer(SponsoringOffer offer) {
        // Vérifier que les champs obligatoires ne sont pas vides ou null
        if (offer.getTitle() == null || offer.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide.");
        }
        if (offer.getDescription() == null || offer.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }
        if (offer.getAmount() < 1000) {
            throw new IllegalArgumentException("Le montant doit être supérieur ou égal à 1000.");
        }
        if (offer.getCreationDate() == null) {
            throw new IllegalArgumentException("La date de création ne peut pas être vide.");
        }

        // Vérifier que la date de création est aujourd’hui ou après
        LocalDate today = LocalDate.now();
        LocalDate creationDate = offer.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (creationDate.isBefore(today)) {
            throw new IllegalArgumentException("La date de création doit être aujourd’hui ou dans le futur.");
        }

        // Fixer la date de création à aujourd'hui si elle est fournie vide
        offer.setCreationDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Définir le statut par défaut
        offer.setStatus(OfferStatus.AVAILABLE);

        return sponsoringOfferRepository.save(offer);
    }

    public SponsoringOffer updateOffer(String id, SponsoringOffer updatedOffer) {
        // Vérifier que les champs obligatoires ne sont pas vides ou null
        if (updatedOffer.getTitle() == null || updatedOffer.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide.");
        }
        if (updatedOffer.getDescription() == null || updatedOffer.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }
        if (updatedOffer.getAmount() < 1000) {
            throw new IllegalArgumentException("Le montant doit être supérieur ou égal à 1000.");
        }
        if (updatedOffer.getCreationDate() == null) {
            throw new IllegalArgumentException("La date de création ne peut pas être vide.");
        }

        // Vérifier que la date de création est aujourd’hui ou après
        LocalDate today = LocalDate.now();
        LocalDate creationLocalDate = updatedOffer.getCreationDate()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (creationLocalDate.isBefore(today)) {
            throw new IllegalArgumentException("La date de création doit être aujourd’hui ou dans le futur.");
        }

        // Mettre à jour l'offre si elle existe
        return sponsoringOfferRepository.findById(id).map(existingOffer -> {
            existingOffer.setTitle(updatedOffer.getTitle());
            existingOffer.setDescription(updatedOffer.getDescription());
            existingOffer.setAmount(updatedOffer.getAmount());
            existingOffer.setStatus(updatedOffer.getStatus());
            existingOffer.setCreationDate(updatedOffer.getCreationDate());
            return sponsoringOfferRepository.save(existingOffer);
        }).orElseThrow(() -> new IllegalArgumentException("Offre non trouvée avec l'ID: " + id));
    }

    public void deleteOffer(String id) {
        sponsoringOfferRepository.deleteById(id);
    }
    @Transactional
    public SponsoringOffer addEventToSponsoringOffer(String offerId, String eventId) {
        // Récupérer les entités
        SponsoringOffer offer = sponsoringOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        // Mise à jour BIDIRECTIONNELLE
        offer.setEventId(eventId);

        // Sauvegarde SYNCHRONE (flush immédiat)
        sponsoringOfferRepository.save(offer);


        // Vérification et génération du contrat
        contractService.checkAndGenerateContract(eventId, offerId);

        return offer;
    }

}