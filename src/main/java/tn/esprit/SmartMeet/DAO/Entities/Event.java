/*
package tn.esprit.SmartMeet.DAO.Entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event")
public class Event {

    @Id
    private String id;
    private String name;
    private String typeE;
    private LocalDate dateE;
    private String lieu;
}*/

package tn.esprit.SmartMeet.DAO.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
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
    @NotBlank(message = "the description is required ")
    @Size(min = 3 , max = 30)
    private String description ;

    private TypeEvent typeEvent ;

    @NotNull(message = "start date is required")
    @FutureOrPresent(message = "start date must be today or in the future")
    private LocalDate dateDebut ;
    @NotNull(message = "end date is required")
    @FutureOrPresent(message = "end date must be today or in the future")
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


    @AssertTrue(message = "end date must be on or after start date")
    private boolean isEndDateValid() {
        if (dateDebut == null || dateFin == null) {
            return true; // Let @NotNull handle null checks
        }
        return !dateFin.isBefore(dateDebut);
    }



}
