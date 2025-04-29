package tn.esprit.SmartMeet.Services.Transport;

import tn.esprit.SmartMeet.DAO.Entities.Transport;
import tn.esprit.SmartMeet.DAO.Entities.TransportAssignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITransportService {
    List<Transport> getAllTransports();

    Optional<Transport> getTransportById(String id);

    Transport createTransport(Transport transport);

    Transport updateTransport(String id, Transport updatedTransport);

    void deleteTransport(String id);
    public boolean isTransportAvailable(String transportId, LocalDate datedebut ,LocalDate datefin);
    public List<TransportAssignment> getTransportAssignments(String transportId);

}

