package tn.esprit.SmartMeet.DAO.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inscriptions")
public class Inscription {
    @Id
    private String id;
    @DBRef
    private User user;
    @DBRef
    private Event event;

    private Instant dateInscription;
}

