package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.SmartMeet.DAO.Entities.CallRequest;

import java.util.List;

public interface CallRequestRepository extends MongoRepository<CallRequest, String> {
    List<CallRequest> findByFromAndTo(String from, String to);
    }