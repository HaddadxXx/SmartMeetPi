package tn.esprit.SmartMeet.DAO.Entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "transport_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransportAssignment {

    @Id
    private String id;
    @DBRef
    private Transport transport;
    @DBRef
    private Event event;
    private LocalDate dateDebut; // Updated to store the start date
    private LocalDate dateFin;   // Updated to store the end date
}