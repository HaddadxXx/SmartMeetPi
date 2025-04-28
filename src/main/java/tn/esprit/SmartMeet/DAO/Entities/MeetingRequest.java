package tn.esprit.SmartMeet.DAO.Entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@Builder
public class MeetingRequest {


    private String title;
    private String description;
    private String startDateTime; // format ISO: "2025-04-23T10:00:00+02:00"
    private String endDateTime;

    private String theme;
    private Long capacite;
    private String lieu;

}
