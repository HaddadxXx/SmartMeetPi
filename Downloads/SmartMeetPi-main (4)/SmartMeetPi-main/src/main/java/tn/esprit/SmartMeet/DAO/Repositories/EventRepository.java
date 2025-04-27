package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.Query;
import tn.esprit.SmartMeet.DAO.Entities.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByContractId(String contractId);


}
