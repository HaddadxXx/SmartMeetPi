package tn.esprit.SmartMeet.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.api.services.calendar.model.ConferenceData;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

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

    @JsonIgnore
    private User user ;

    private Double budget;

    private String contractId; // 💡 Nouveau champ

    private String sponsoringOfferId;
    private List<SponsorshipRequest> sponsorRequests;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<User> getParticipants() {
        return  getParticipants() ;
    }

    @DBRef  // Cette annotation permet d'utiliser une référence vers un autre document dans MongoDB
    @JsonIgnore  // Permet de sérialiser la relation de l'événement vers les utilisateurs
    private List<User> participants;

    @Transient
    private int nbParticipations;

    @Transient
    private int tendanceRank;

    public int getNbParticipations() {
        return nbParticipations;
    }

    public void setNbParticipations(int nbParticipations) {
        this.nbParticipations = nbParticipations;
    }

    public int getTendanceRank() {
        return tendanceRank;
    }

    public void setTendanceRank(int tendanceRank) {
        this.tendanceRank = tendanceRank;
    }

    private double pourcentageParticipation;

    public void setParticipations(List<Participate> participations) {
        this.participants =participants ;
    }
}

