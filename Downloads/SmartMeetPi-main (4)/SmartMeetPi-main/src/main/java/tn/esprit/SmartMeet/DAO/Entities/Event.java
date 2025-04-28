package tn.esprit.SmartMeet.DAO.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@Document(collection = "events") // Collection MongoDB
public class Event {
    @Id
    private String id;
    private String name;
    private String description;
    private Date date;
    private String contractId; // 💡 Nouveau champ
    private Double budget;


    private String sponsoringOfferId;
    private List<SponsorshipRequest> sponsorRequests;
}
