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
@EqualsAndHashCode
public class Ressource {
    @Id
    private String id; // ID généré automatiquement
    @NotBlank(message = "The resource name is required")
    @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters long")
    private String name;
    @NotBlank(message = "The resource type is required")
    private String typeR; // Ex: Salle, Matériel, etc.
    @Min(value = 1, message = "The quantity must be greater than zero")
    private long quantite;
    @NotBlank(message = "The status of the resource is required")
    private String statutR;
}
