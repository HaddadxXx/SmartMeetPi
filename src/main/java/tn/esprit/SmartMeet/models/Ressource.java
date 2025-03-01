package tn.esprit.SmartMeet.models;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Le nom de la ressource est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String name;
    @NotBlank(message = "Le type de ressource est obligatoire")
    private String typeR; // Ex: Salle, Matériel, etc.
    @Min(value = 1, message = "La quantité doit être supérieure à zéro")
    private long quantite;
    @NotBlank(message = "Le statut de la ressource est obligatoire")
    private String statutR;
}
