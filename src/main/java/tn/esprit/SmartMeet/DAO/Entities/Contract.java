package tn.esprit.SmartMeet.DAO.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@Document(collection = "contracts")
public class Contract {
    @Id
    private String id;
    private String eventId;
    private String sponsoringOfferId;
    private String title;
    private String description;
    private double amount;
    private Date creationDate;
}
