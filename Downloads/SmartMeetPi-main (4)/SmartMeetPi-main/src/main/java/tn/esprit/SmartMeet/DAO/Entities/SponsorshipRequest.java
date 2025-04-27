package tn.esprit.SmartMeet.DAO.Entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "sponsorshipRequests")//utilisée pour marquer une classe comme étant un modèle de données mappé à un document dans une base de données MongoDB
@Getter
@Setter
public class SponsorshipRequest {
    @Id
    private String id;
    private String eventDescription;
    private RequestStatus statusR; // Utilisation de l'énumération
    private Date requestDate;
    // Getter et Setter pour statusR (si nécessaire)

    // Getter et Setter pour eventDescription
    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public RequestStatus getStatus() {
        return statusR;
    }

    public void setStatus(RequestStatus statusR) {
        this.statusR = statusR;
    }
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
    public Date getRequestDate() {
        return requestDate;
    }

}
