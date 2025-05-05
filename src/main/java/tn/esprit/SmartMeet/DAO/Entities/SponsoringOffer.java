package tn.esprit.SmartMeet.DAO.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Date;
import java.util.List;

@Document(collection = "sponsoringOffers") // Nom de la collection dans MongoDB
@Getter
@Setter
@Data
public class SponsoringOffer {
    @Id
    private String id;
    private String title;
    private String description;
    private double amount;
    private Date creationDate;
    private OfferStatus status;
    private String client; // 👉 juste l'email du client, pas l'objet User

    private String contractId;
    private String eventId;



    public void setUpdateCreationDate(Date date) {
    }
    // Getters et Setters
}
