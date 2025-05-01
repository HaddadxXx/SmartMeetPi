package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.DAO.Entities.Participate;

import java.util.List;

@Repository
public interface ParticipateRepository extends MongoRepository<Participate , String> {

    long countByEvent_IdEvent(String idEvent);

    List<Participate> findByEvent_IdEvent(String eventId);

   // List<Participate> findByEventId(String eventId);

    //   List<Participate> findByEvent_User_Id(String userId);


 //   List<Participate> findByEventId(String eventId);
}
