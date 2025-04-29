package tn.esprit.SmartMeet.DAO.Repositories;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.DAO.Entities.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepo extends MongoRepository<Event, String> {
    Optional<Event> findById(String id);
    @Query("{ 'transports.id' : ?0 }")
    List<Event> findByTransportsId(String transportId);


}
