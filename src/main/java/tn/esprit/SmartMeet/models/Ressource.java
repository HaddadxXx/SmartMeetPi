package tn.esprit.SmartMeet.models;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "ressources") // Collection MongoDB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ressource {
    @Id
    private String id; // ID généré automatiquement
    private String name;
    private String typeR; // Ex: Salle, Matériel, etc.
    private long quantite;
    private String statutR;
}
