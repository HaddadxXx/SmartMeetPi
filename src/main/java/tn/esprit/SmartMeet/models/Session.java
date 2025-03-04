package tn.esprit.SmartMeet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    private String titre;
    private String date;


    @JsonIgnore
    @DBRef
    private Event evenement;


}
