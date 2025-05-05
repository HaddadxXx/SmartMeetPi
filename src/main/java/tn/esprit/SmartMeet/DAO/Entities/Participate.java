package tn.esprit.SmartMeet.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Document(collection = "Participate ")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participate {


    @Id
    private String idParticpate ;

    private String filePath;

    @DBRef
    @JsonIgnore
    private User user;


    @DBRef
    private Event event;

    private LocalDate dateOfParticpation ;


}
