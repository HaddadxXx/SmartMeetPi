package tn.esprit.SmartMeet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.Repositories.TransportRepo;
import tn.esprit.SmartMeet.models.Transport;

import java.util.List;
import java.util.Optional;

@Service
public class TransportService {
    @Autowired
    private TransportRepo transportRepository;

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

    public void deleteTransport(String id) {
        transportRepository.deleteById(id);
    }
}
