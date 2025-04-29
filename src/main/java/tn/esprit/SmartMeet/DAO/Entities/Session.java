package tn.esprit.SmartMeet.DAO.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "session") // Collection MongoDB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    private String idSession; // ID généré automatiquement id
    private String titre; // nameSession
    private LocalDate date; //dateSession
    private List<Ressource> ressources = new ArrayList<>(); // Stocke les objets Ressource
}
