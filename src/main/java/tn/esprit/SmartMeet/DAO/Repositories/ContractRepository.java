package tn.esprit.SmartMeet.DAO.Repositories;
import tn.esprit.SmartMeet.DAO.Entities.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends MongoRepository<Contract, String> {
    Contract  findByEventIdAndSponsoringOfferId(String eventId, String sponsoringOfferId);}
