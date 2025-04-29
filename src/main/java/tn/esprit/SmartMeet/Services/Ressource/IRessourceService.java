package tn.esprit.SmartMeet.Services.Ressource;
import tn.esprit.SmartMeet.DAO.Entities.*;

import java.util.List;
import java.util.Optional;

public interface IRessourceService {
    List<Ressource> getAllRessources();
    Optional<Ressource> getRessourceById(String id);
    Ressource createRessource(Ressource ressource);
    Ressource updateRessource(String id, Ressource updatedRessource);
    void deleteRessource(String id);
}
