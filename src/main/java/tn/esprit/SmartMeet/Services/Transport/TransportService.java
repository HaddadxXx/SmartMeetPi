package tn.esprit.SmartMeet.Services.Transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Repositories.*;
import tn.esprit.SmartMeet.DAO.Entities.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransportService implements ITransportService{
    @Autowired
    private TransportRepo transportRepository;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private TransportAssignmentRepo transportAssignmentRepo;

    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    public Optional<Transport> getTransportById(String id) {
        return transportRepository.findById(id);
    }

    public Transport createTransport(Transport transport) {
        return transportRepository.save(transport);
    }

    public Transport updateTransport(String id, Transport updatedTransport) {
        updatedTransport.setId(id);
        return transportRepository.save(updatedTransport);
    }

  //  public void deleteTransport(String id) {transportRepository.deleteById(id);}

    public void deleteTransport(String id) {
        // Delete associated assignments first
        List<TransportAssignment> assignments = transportAssignmentRepo.findByTransportId(id);
        transportAssignmentRepo.deleteAll(assignments);
        // Then delete the transport
        transportRepository.deleteById(id);
    }

    public boolean isTransportAvailable(String transportId, LocalDate datedebut ,LocalDate datefin) {
        List<TransportAssignment> assignments = transportAssignmentRepo.findByTransportIdAndDateRange(transportId, datedebut, datefin);
        return assignments.isEmpty();
    }

    public List<TransportAssignment> getTransportAssignments(String transportId) {
        return transportAssignmentRepo.findByTransportId(transportId);
    }




}

