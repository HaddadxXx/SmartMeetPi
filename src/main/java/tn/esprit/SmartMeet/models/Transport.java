package tn.esprit.SmartMeet.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Le type de transport est obligatoire")
    @Size(min = 2, max = 50, message = "Le type doit contenir entre 2 et 50 caractères")
    private String type; // Ex: Bus, Voiture, Navette
    @Min(value = 1, message = "La quantité doit être supérieure à zéro")
    private int capacite; // Nombre de places
    @NotBlank(message = "Le statut du transport est obligatoire")
    private String statut;
}
