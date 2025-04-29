package tn.esprit.SmartMeet.Services.Event;


import com.google.api.client.json.JsonFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.*;

import com.google.api.client.json.JsonFactory;

import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.DAO.Repositories.ParticipateRepository;
import tn.esprit.SmartMeet.DAO.Repositories.SessionRepository;
import tn.esprit.SmartMeet.DAO.Repositories.UserRepository;
import tn.esprit.SmartMeet.DAO.Repositories.ParticipateRepository;

import java.io.File;
import java.io.IOException;

import com.google.api.client.json.JsonFactory;
import tn.esprit.SmartMeet.Utils.GoogleAuthorizeUtil;
import com.google.api.services.calendar.model.EntryPoint;

import javax.imageio.spi.IIORegistry;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/events")
@CrossOrigin
@Service
public class EventService implements IEventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final SessionRepository sessionRepository;
    private final ParticipateRepository participateRepository;

    public EventService(UserRepository userRepository, EventRepository eventRepository, SessionRepository sessionRepository, ParticipateRepository participateRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.sessionRepository = sessionRepository;
        this.participateRepository = participateRepository;
    }

    @Override
    public Event addEvenement(Event evenement, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads/" + filename);
                Files.write(path, file.getBytes());
                evenement.setPhoto(filename); // Stocke juste le nom ou le chemin relatif
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserId = authentication.getName(); // ou autre selon ton système d'authentification
            evenement.setOwnerId(currentUserId);
            //   System.out.println("Tentative d'ajout dans la base : " + evenement);
            return eventRepository.save(evenement);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'upload de la photo");
        }
    }

    @Override
    public User getUserByOwnerEvent(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        return event.getUser(); // récupère le propriétaire (owner)
    }


    /* @Override
     public List<Event> getEventsByOwner( String ownerId){
         List<Event> events = eventRepository.findByOwnerId(ownerId);
         return eventRepository.findByOwnerId(ownerId);
     }*/
    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(String id) {
        return null;
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event updateEvent(String id, Event event) {
        Event existingEvent = eventRepository.findById(id).orElse(null);

        if (existingEvent != null) {
            // Mettre à jour les champs non nulls
            if (event.getNomEvent() != null) existingEvent.setNomEvent(event.getNomEvent());
            if (event.getTheme() != null) existingEvent.setTheme(event.getTheme());
            if (event.getDescription() != null) existingEvent.setDescription(event.getDescription());
            if (event.getTypeEvent() != null) existingEvent.setTypeEvent(event.getTypeEvent());
            if (event.getSessions() != null) existingEvent.setSessions(event.getSessions());

            // Vérifie si la capacité a une valeur valide avant de l'appliquer
            if (event.getCapacite() != null) {
                existingEvent.setCapacite(event.getCapacite());
            }

            // Mettre à jour les autres champs si nécessaire, comme la date
            if (event.getDateDebut() != null) existingEvent.setDateDebut(event.getDateDebut());
            if (event.getDateFin() != null) existingEvent.setDateFin(event.getDateFin());

            // Sauvegarde l'événement mis à jour
            return eventRepository.save(existingEvent);
        } else {
            throw new RuntimeException("Event not found with ID: " + id);
        }
    }

    @Override
    public Session ajouterSessionEtAffecterAEvenement(Session session, String eventName) {
        // Rechercher l'événement par son nom
        Event nomEvent = eventRepository.findByNomEvent(eventName);
        if (nomEvent == null) {
            throw new RuntimeException("Aucun événement trouvé avec le nom : " + eventName);
        }


        // Sauvegarder la session si elle n'existe pas encore
        session = sessionRepository.save(session);
        // Vérifier et initialiser la liste des sessions
        if (nomEvent.getSessions() == null) {
            nomEvent.setSessions(new ArrayList<>());
        }

        // Ajouter la session à l'événement
        nomEvent.getSessions().add(session);

        // Sauvegarder l'événement mis à jour
        eventRepository.save(nomEvent);

        return session;
    }


    @Override
    public void verifierEtatEvenement(String eventId) {
        // Récupérer l'événement choisi par le participant
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));

        // Récupérer la capacité et le nombre de participants
        Long capacite = event.getCapacite();
        long nbParticipants = participateRepository.countByEvent_IdEvent(eventId);
        double taux = ((double) nbParticipants / capacite) * 100;

        if (taux < 20) {
            // Récupérer les participants
            List<Participate> participations = participateRepository.findByEvent_IdEvent(eventId);
            for (Participate participation : participations) {
                User participant = participation.getUser();
                participant.setScoreReputation(participant.getScoreReputation() + 20);
                userRepository.save(participant); // Sauvegarder les modifications
            }

            // Supprimer l'événement
            eventRepository.deleteById(eventId);
            System.out.println("Événement supprimé (moins de 20%) + bonus de réputation ajouté");
        } else if (taux < 50) {
            // Supprimer toutes les sessions liées
            if (event.getSessions() != null) {
                event.getSessions().forEach(session -> sessionRepository.deleteById(session.getIdSession()));
            }
            event.setSessions(null);
            eventRepository.save(event);
            System.out.println("Sessions supprimées (moins de 50%)");
        } else {
            System.out.println("Aucune action nécessaire (50% ou plus)");
        }
    }

    @Override
    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    /* public Participate participateToEvent(String email, String eventId) {
         User user = userRepository.findByEmail(email)
                 .orElseThrow(() -> new RuntimeException("User not found"));
         Event event = eventRepository.findById(eventId)
                 .orElseThrow(() -> new RuntimeException("Event not found"));
         // Affichage de l'id utilisateur dans la console
       //  System.out.println("User ID: " + user.getId());
         Participate participate = new Participate();
         participate.setUser(user);
         participate.setEvent(event);
         participate.setDateOfParticpation(LocalDate.now());

         return participateRepository.save(participate);
     }*/
/*   public Participate participateToEvent(String email, String eventId, MultipartFile file) {
       User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new RuntimeException("User not found"));

       Event event = eventRepository.findById(eventId)
               .orElseThrow(() -> new RuntimeException("Event not found"));

       Participate participate = new Participate();
       participate.setUser(user);
       participate.setEvent(event);
       participate.setDateOfParticpation(LocalDate.now());

       if (file != null && !file.isEmpty()) {
           try {
               // ✅ Chemin absolu dynamique vers le dossier "uploads/participation" dans le projet
               String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator ;

               File uploadPath = new File(uploadDir);
               if (!uploadPath.exists()) {
                   boolean created = uploadPath.mkdirs();
                   if (!created) {
                       throw new RuntimeException("Could not create upload directory: " + uploadDir);
                   }
               }

               // ✅ Nom unique
               String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

               // ✅ Destination complète
               File destinationFile = new File(uploadPath, fileName);
               file.transferTo(destinationFile);

               System.out.println("Fichier reçu : " + file.getOriginalFilename());

               // ✅ Chemin relatif stocké
               participate.setFilePath("uploads/" + fileName);

           } catch (IOException e) {
               throw new RuntimeException("Error while uploading file: " + e.getMessage());
           }
       }

       return participateRepository.save(participate);
   }
*/
    public Participate participateToEvent(String email, String eventId, MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Participate participate = new Participate();
        participate.setUser(user);
        participate.setEvent(event);
        participate.setDateOfParticpation(LocalDate.now());


        if (file != null && !file.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                String uploadDir = System.getProperty("user.dir") + "/uploads"; // Dossier racine
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                File destinationFile = new File(uploadDir + "/" + fileName);
                file.transferTo(destinationFile);

                participate.setFilePath(fileName); // ou file.getAbsolutePath() si tu veux le chemin complet

            } catch (IOException e) {
                throw new RuntimeException("Error while uploading file: " + e.getMessage());
            }
        }

        return participateRepository.save(participate);
    }

    @Override
    public Event createEventWithMeetLink(MeetingRequest request, MultipartFile file) throws Exception {
        // 1. Connexion à Google Calendar API
        Credential credential = GoogleAuthorizeUtil.authorize();
        Calendar calendarService = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("SmartMeet").build();

        // 2. Création de l'événement Google Calendar avec conférence (Meet)
        com.google.api.services.calendar.model.Event googleEvent = new com.google.api.services.calendar.model.Event()
                .setSummary(request.getTitle())
                .setDescription(request.getDescription())
                .setStart(new EventDateTime()
                        .setDateTime(new DateTime(request.getStartDateTime()))
                        .setTimeZone("Africa/Tunis"))
                .setEnd(new EventDateTime()
                        .setDateTime(new DateTime(request.getEndDateTime()))
                        .setTimeZone("Africa/Tunis"))
                .setConferenceData(new ConferenceData()
                        .setCreateRequest(new CreateConferenceRequest()
                                .setRequestId(UUID.randomUUID().toString())));

        googleEvent = calendarService.events()
                .insert("primary", googleEvent)
                .setConferenceDataVersion(1)
                .execute();

        // 3. Récupération du lien Google Meet
        String meetLink = googleEvent.getConferenceData().getEntryPoints().get(0).getUri();

        // 4. Gestion du fichier uploadé (photo)
        String photoFilename = null;
        if (file != null && !file.isEmpty()) {
            try {
                photoFilename = "event_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("uploads"); // juste 'uploads' à la racine du projet
                Files.createDirectories(uploadPath);
                Files.copy(file.getInputStream(), uploadPath.resolve(photoFilename), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file: " + e.getMessage());
            }
        }

        // 5. Traitement des dates et horaires
        LocalDate startDate = LocalDate.parse(request.getStartDateTime().substring(0, 10));
        LocalDate endDate = LocalDate.parse(request.getEndDateTime().substring(0, 10));
        String startTime = request.getStartDateTime().substring(11, 16);
        String endTime = request.getEndDateTime().substring(11, 16);
        String timeRange = startTime + " - " + endTime;

        // 6. Création de l'entité Event (celle de ta base de données)
        Event event = Event.builder()
                .nomEvent(request.getTitle())
                .theme(request.getTheme())
                .description(request.getDescription())
                .typeEvent(TypeEvent.ENLIGNE)
                .dateDebut(startDate)
                .dateFin(endDate)
                .horaire(timeRange)
                .capacite(request.getCapacite() != null ? request.getCapacite() : 50L)
                .lieu(request.getLieu() != null ? request.getLieu() : "En ligne")
                .photo(photoFilename)
                .meetLink(meetLink)
                .build();

        // 7. Sauvegarde dans ta base
        return eventRepository.save(event);
    }


    public void afficherParticipantsParCreateur(String userId) {
        List<Participate> participations = participateRepository.findByEvent_User_Id(userId);

        for (Participate p : participations) {
            String participantName = p.getUser().getFirstName() + " " + p.getUser().getLastName();
            String eventName = p.getEvent().getNomEvent();
            String eventOwner = p.getEvent().getUser().getFirstName() + " " + p.getEvent().getUser().getLastName();

            System.out.println("Participant : " + participantName);
            System.out.println("Événement : " + eventName);
            System.out.println("Créé par : " + eventOwner);
            System.out.println("-------------------------------");
        }


    }

    @Override
    public String lancerMeetPourEvent(String eventId) {
        try {
            // 1. Chercher l'événement MongoDB
            tn.esprit.SmartMeet.DAO.Entities.Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Événement introuvable avec l'ID: " + eventId));

            // 2. Vérifier si c'est bien un événement en ligne
            if (event.getTypeEvent() != TypeEvent.ENLIGNE) {
                throw new RuntimeException("Seuls les événements en ligne peuvent lancer un Google Meet.");
            }

            // 3. Se connecter à Google Calendar
            Credential credential = GoogleAuthorizeUtil.authorize();
            Calendar service = new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
            ).setApplicationName("My App").build();

            // 4. Créer l'événement Google Calendar
            com.google.api.services.calendar.model.Event googleEvent = new com.google.api.services.calendar.model.Event()
                    .setSummary(event.getNomEvent())
                    .setDescription(event.getDescription())
                    .setStart(new EventDateTime()
                            .setDateTime(new DateTime(event.getDateDebut().atTime(10, 0).toString() + ":00Z")))
                    .setEnd(new EventDateTime()
                            .setDateTime(new DateTime(event.getDateDebut().atTime(11, 0).toString() + ":00Z")))
                    .setConferenceData(new ConferenceData()
                            .setCreateRequest(new CreateConferenceRequest()
                                    .setRequestId(UUID.randomUUID().toString())));

            googleEvent = service.events().insert("primary", googleEvent)
                    .setConferenceDataVersion(1)
                    .execute();

            // 5. Récupérer le lien Meet
            String meetLink = googleEvent.getHangoutLink();

            // 6. Sauvegarder le lien dans l'événement MongoDB
            event.setMeetLink(meetLink);
            eventRepository.save(event);

            // 7. Retourner le lien
            return meetLink;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du lancement du Meet : " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getParticipantsEmails(String eventId) {
        try {
            System.out.println("Recherche des participants pour l'eventId : " + eventId);
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Événement non trouvé"));
            System.out.println("Événement trouvé : " + event.getNomEvent());
            return event.getParticipants().stream()
                    .map(User::getEmail)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            throw e;
        }
    }
}






















