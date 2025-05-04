package tn.esprit.SmartMeet.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data  // Génère les getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    private String idSession ;
    @NotBlank(message = "the title is required ")
    @Size(min = 1 , max = 10)
    private String titre;
    @NotBlank(message = "the date is required ")
    private String date;


    @JsonIgnore
    @DBRef
    private Event evenement;

    private String nomEvent;

    public void setNomEvent(String nomEvent) {
        this.nomEvent = nomEvent; // Implémentation correcte du setter
    }
}
