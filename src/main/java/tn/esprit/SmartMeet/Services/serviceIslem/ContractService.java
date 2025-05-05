package tn.esprit.SmartMeet.Services.serviceIslem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.SmartMeet.DAO.Entities.Contract;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import tn.esprit.SmartMeet.DAO.Entities.SponsoringOffer;
import tn.esprit.SmartMeet.DAO.Repositories.ContractRepository;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.DAO.Repositories.SponsoringOfferRepository;

import java.util.Date;
import java.util.Optional;
@CrossOrigin
@Service
@Transactional

public class ContractService {
    @Autowired
    private SponsoringOfferRepository sponsoringOfferRepository;
    @Autowired
    private final ContractRepository contractRepository;

    @Autowired
    private EventRepository eventRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Async
    @Transactional
    public void checkAndGenerateContract(String eventId, String offerId) {
        try {
            // Recharger les entités depuis la base de données
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
            SponsoringOffer offer = sponsoringOfferRepository.findById(offerId)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

            // Logs de débogage détaillés
            System.out.println("\n=== VÉRIFICATION MATCHING ===");
            System.out.println("Event ID: " + eventId);
            System.out.println("SponsoringOffer ID: " + offerId);
            System.out.println("SponsoringOfferId de l'event: " + event.getSponsoringOfferId());
            System.out.println("EventId de l'offre: " + offer.getEventId());

            // Vérification bidirectionnelle
            if (event.getSponsoringOfferId().equals(offerId) && offer.getEventId().equals(eventId)) {
                System.out.println("Matching confirmé ! Génération du contrat...");

                // Vérifier si un contrat existe déjà
                Optional<Contract> Contract = Optional.ofNullable(contractRepository.findByEventIdAndSponsoringOfferId(eventId, offerId));
                if (Contract.isPresent()) {
                    System.out.println("Un contrat existe déjà pour cette association");
                    return;
                }

                // Création du nouveau contrat
                Contract contract = new Contract();
                contract.setEventId(eventId);
                contract.setSponsoringOfferId(offerId);
                contract.setTitle("Contrat entre " + event.getNomEvent() + " et " + offer.getTitle());
                contract.setAmount(offer.getAmount());
                contract.setDescription(contract.getDescription());
                contract.setCreationDate(new Date());

                // Sauvegarde du contrat
                Contract savedContract = contractRepository.save(contract);
                System.out.println("Contrat généré avec ID: " + savedContract.getId());

                // Mise à jour des relations
                event.setContractId(savedContract.getId());
                offer.setContractId(savedContract.getId());

                eventRepository.save(event);
                sponsoringOfferRepository.save(offer);

                System.out.println("Mise à jour des entités effectuée");
            } else {
                System.out.println("Aucun matching - Raisons possibles :");
                System.out.println("Event.sponsoringOfferId != offerId => " + (!event.getSponsoringOfferId().equals(offerId)));
                System.out.println("Offer.eventId != eventId => " + (!offer.getEventId().equals(eventId)));
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du contrat : " + e.getMessage());
            e.printStackTrace();
        }
    }




}