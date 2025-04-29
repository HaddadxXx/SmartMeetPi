package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.SmartMeet.DAO.Entities.TransportAssignment;

import java.time.LocalDate;
import java.util.List;

public interface TransportAssignmentRepo extends MongoRepository<TransportAssignment, String> {
    List<TransportAssignment> findByTransportId(String transportId);

    // Updated to find assignments where the period overlaps with the given range
    @Query("{ 'transport.id': ?0, $or: [ " +
            "{ 'dateDebut': { $lte: ?2 }, 'dateFin': { $gte: ?1 } }, " + // Overlap: new period starts before existing ends
            "{ 'dateDebut': { $gte: ?1, $lte: ?2 } }, " +               // Overlap: existing starts within new period
            "{ 'dateFin': { $gte: ?1, $lte: ?2 } } " +                 // Overlap: existing ends within new period
            "] }")
    List<TransportAssignment> findByTransportIdAndDateRange(String transportId, LocalDate dateDebut, LocalDate dateFin);
}