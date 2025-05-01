package tn.esprit.SmartMeet.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.api.services.calendar.model.ConferenceData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Document
@Data// Génère les getters, setters, toString, etc.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    private String idEvent;
    @NotBlank(message = "the name is required ")
    @Size(min = 1 , max = 10)
    @Indexed(unique = true)
    private String nomEvent;
    @NotBlank(message = "the theme is required ")
    @Size(min = 1 , max = 10)
    private String theme ;
    @NotBlank(message = "the theme is required ")
    @Size(min = 3 , max = 30)
    private String description ;
    
    private TypeEvent typeEvent ;

    private LocalDate dateDebut ;
    private LocalDate dateFin;
    @Min(10)
    private Long capacite ;

    private String horaire ;
    private  String lieu  ;


    private String photo;
    private String meetLink;
    private String ownerId;

    @JsonIgnore
    @DBRef
    private List<Session> sessions;


    @DBRef
    @JsonIgnore
    private User user ;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setConferenceData(ConferenceData conferenceData) {
    }

/*    public Group setSummary(String summary) {
        return null;
    }*/

   /* public List<User> getParticipants() {
        if (participations == null) return List.of();
        return participations.stream()
                .map(Participate::getUser)
                .collect(Collectors.toList());
    }*/

    @DBRef
    @JsonIgnore
    private List<Participate> participations;


}

