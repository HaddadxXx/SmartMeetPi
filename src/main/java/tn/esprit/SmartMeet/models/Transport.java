package tn.esprit.SmartMeet.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transport {
    @Id
    private String id; // ID MongoDB
    private String type; // Ex: Bus, Voiture, Navette
    private int capacite; // Nombre de places
    private String statut;
}
