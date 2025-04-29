package tn.esprit.SmartMeet.Services.Event;
import com.google.api.services.calendar.model.EntryPoint;
import com.google.api.services.calendar.model.Event;
import tn.esprit.SmartMeet.DAO.Entities.MeetingRequest;
import tn.esprit.SmartMeet.DAO.Entities.TypeEvent;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import tn.esprit.SmartMeet.DAO.Repositories.EventRepository;
import tn.esprit.SmartMeet.Utils.GoogleAuthorizeUtil;
//import tn.esprit.SmartMeet.DAO.Entities.Event;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@Service
public class GoogleCalendarService {
    @Autowired
    private EventRepository eventRepository;

    private static final String APPLICATION_NAME = "Spring Meet Integration";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/calendar.events");

    public Calendar getCalendarService() throws Exception {
        Credential credential = getCredentials();
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential
        ).setApplicationName(APPLICATION_NAME).build();
    }

    private Credential getCredentials() throws Exception {
        InputStream in = GoogleCalendarService.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new RuntimeException("Le fichier credentials.json est introuvable dans src/main/resources/");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                SCOPES
        )
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get(TOKENS_DIRECTORY_PATH).toFile()))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver.Builder().setPort(8888).build())
                .authorize("user");
    }



   /* public String createEventWithMeet(String title, String description, String start, String end)
            throws Exception {

        Credential credential = GoogleAuthorizeUtil.authorize(); // tu dois avoir cette méthode

        Calendar service = new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("My App").build();

        Event event = new Event()
                .setSummary(title)
                .setDescription(description);

        event.setStart(new EventDateTime().setDateTime(new DateTime(start)));
        event.setEnd(new EventDateTime().setDateTime(new DateTime(end)));

        // Ajouter le Google Meet
        CreateConferenceRequest conferenceRequest = new CreateConferenceRequest()
                .setRequestId(UUID.randomUUID().toString());
        ConferenceData conferenceData = new ConferenceData()
                .setCreateRequest(conferenceRequest);
        event.setConferenceData(conferenceData);

        // Insérer l’événement
        event = service.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        return event.getHangoutLink(); // Le lien Google Meet
    }*/


 public String createEventWithMeet(MeetingRequest request) throws Exception {
     Credential credential = GoogleAuthorizeUtil.authorize();

     Calendar service = new Calendar.Builder(
             GoogleNetHttpTransport.newTrustedTransport(),
             JacksonFactory.getDefaultInstance(),
             credential
     ).setApplicationName("My App").build();

     // Création de l'événement
     Event googleEvent = new Event()
             .setSummary(request.getTitle())
             .setDescription(request.getDescription())
             .setStart(new EventDateTime().setDateTime(new DateTime(request.getStartDateTime())))
             .setEnd(new EventDateTime().setDateTime(new DateTime(request.getEndDateTime())))
             .setConferenceData(new ConferenceData()
                     .setCreateRequest(new CreateConferenceRequest()
                             .setRequestId(UUID.randomUUID().toString())));

     googleEvent = service.events().insert("primary", googleEvent)
             .setConferenceDataVersion(1)
             .execute();

     String meetLink = googleEvent.getHangoutLink();

     // Conversion des dates et horaires
     LocalDate dateDebut = LocalDate.parse(request.getStartDateTime().substring(0, 10));
     LocalDate dateFin = LocalDate.parse(request.getEndDateTime().substring(0, 10));
     String horaire = request.getStartDateTime().substring(11, 16) + " - " +
             request.getEndDateTime().substring(11, 16);

     // Création de l'objet MongoDB
     tn.esprit.SmartMeet.DAO.Entities.Event eventToSave = tn.esprit.SmartMeet.DAO.Entities.Event.builder()
             .nomEvent(request.getTitle())
             .description(request.getDescription())
             .theme(request.getTheme())
             .dateDebut(dateDebut)
             .dateFin(dateFin)
             .horaire(horaire)
             .meetLink(meetLink)
             .capacite(request.getCapacite() != null ? request.getCapacite() : 50L)
             .typeEvent(TypeEvent.ENLIGNE)
             .lieu(request.getLieu() != null ? request.getLieu() : "En ligne")
             .build();

     eventRepository.save(eventToSave);

     return meetLink;
 }



}
