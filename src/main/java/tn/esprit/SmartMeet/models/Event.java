package tn.esprit.SmartMeet.models;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "event") // Collection MongoDB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    private String id; // ID généré automatiquement
    private String name;
    private String typeE; // Ex: Réunion, Conférence, etc.
    private LocalDate dateE;
    private String lieu;
    private List<Transport> transports = new ArrayList<>(); // Stocke les objets Transport

}
