package tn.esprit.SmartMeet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.Repositories.RessourceRepo;
import tn.esprit.SmartMeet.models.Ressource;

import java.util.List;
import java.util.Optional;

@Service

public class RessourceService {
    @Autowired
    private RessourceRepo ressourceRepository;

    public List<Ressource> getAllRessources() {
        return ressourceRepository.findAll();
    }

    public Optional<Ressource> getRessourceById(String id) {
        return ressourceRepository.findById(id);
    }

    public Ressource createRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    public Ressource updateRessource(String id, Ressource updatedRessource) { updatedRessource.setId(id);
        return ressourceRepository.save(updatedRessource);
    }

    public void deleteRessource(String id) {
        ressourceRepository.deleteById(id);
    }
}
