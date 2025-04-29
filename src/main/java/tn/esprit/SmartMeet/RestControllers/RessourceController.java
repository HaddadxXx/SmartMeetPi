package tn.esprit.SmartMeet.RestControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Ressource;
import tn.esprit.SmartMeet.Services.Ressource.IRessourceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ressources")
@CrossOrigin(origins = "http://localhost:4200")
public class RessourceController {
    @Autowired
    private IRessourceService ressourceService;

    @GetMapping("/all")
    public List<Ressource> getAllRessources() {
        return ressourceService.getAllRessources();
    }

    @GetMapping("/{id}")
    public Optional<Ressource> getRessourceById(@PathVariable String id) {
        return ressourceService.getRessourceById(id);
    }

    @PostMapping("/create")
    public Ressource createRessource(@Valid @RequestBody Ressource ressource) {
        return ressourceService.createRessource(ressource);
    }

    @PutMapping("/update/{id}")
    public Ressource updateRessource(@PathVariable String id,@Valid @RequestBody Ressource ressource) {
        return ressourceService.updateRessource(id, ressource);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRessource(@PathVariable String id) {
        ressourceService.deleteRessource(id);
    }
}
