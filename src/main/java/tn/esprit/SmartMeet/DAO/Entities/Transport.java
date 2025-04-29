package tn.esprit.SmartMeet.DAO.Entities;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "transport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Transport {
    @Id
    private String id; // ID MongoDB
    @NotBlank(message = "The transport type is required")
    @Size(min = 2, max = 50, message = "The type must contain between 2 and 50 characters")
    private String type; // Ex: Bus, Voiture, Navette
    @Min(value = 1, message = "The capacity must be greater than zero")
    private int capacite; // Nombre de places
    @NotBlank(message = "The transport status is required")
    private String statut;
    @Transient
    private List<TransportAssignment> assignments = new ArrayList<>();
}