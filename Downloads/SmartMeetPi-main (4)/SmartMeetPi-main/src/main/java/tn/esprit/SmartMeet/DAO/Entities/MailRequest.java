package tn.esprit.SmartMeet.DAO.Entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MailRequest {
    private String to;
    private String subject;
    private String body;
    private String attachmentPath; // facultatif

    // Getters + Setters
}
