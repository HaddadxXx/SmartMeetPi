/*package tn.esprit.SmartMeet.RestControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.Services.Transport.ITransportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/transports")
public class TransportController {


    @Autowired
    private ITransportService transportService;

    @GetMapping("/all")
    public List<Transport> getAllTransports() {
        return transportService.getAllTransports();
    }
    @GetMapping("/{id}")
    public Optional<Transport> getTransportById(@PathVariable String id) {
        return transportService.getTransportById(id);
    }

    @PostMapping("/create")
    public Transport createTransport(@Valid @RequestBody Transport transport) {
        return transportService.createTransport(transport);
    }

    @PutMapping("/update/{id}")
    public Transport updateTransport(@Valid @PathVariable String id, @RequestBody Transport transport) {
        return transportService.updateTransport(id, transport);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTransport(@PathVariable String id) {
        transportService.deleteTransport(id);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        boolean isAvailable = transportService.isTransportAvailable(id, date);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<TransportAssignment>> getAssignments(@PathVariable String id) {
        List<TransportAssignment> assignments = transportService.getTransportAssignments(id);
        return ResponseEntity.ok(assignments);
    }

}*/

package tn.esprit.SmartMeet.RestControllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Transport;
import tn.esprit.SmartMeet.DAO.Entities.TransportAssignment;
import tn.esprit.SmartMeet.DAO.Repositories.TransportAssignmentRepo;
import tn.esprit.SmartMeet.DAO.Repositories.TransportRepo;
import tn.esprit.SmartMeet.Services.Transport.ITransportService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transports")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TransportController {

    private ITransportService transportService;
    private TransportRepo transportRepo;
    private TransportAssignmentRepo transportAssignmentRepo;

    @PostMapping("/create")
    public Transport createTransport(@Valid @RequestBody Transport transport) {
        return transportService.createTransport(transport);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transport>> getAllTransports() {
        return ResponseEntity.ok(transportService.getAllTransports());
    }

    @PutMapping("/update/{id}")
    public Transport updateTransport(@Valid @PathVariable String id, @RequestBody Transport transport) {
        return transportService.updateTransport(id, transport);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransport(@PathVariable String id) {
        transportService.deleteTransport(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assignments/{transportId}")
    public ResponseEntity<List<TransportAssignment>> getAssignments(@PathVariable String transportId) {
        List<TransportAssignment> assignments = transportAssignmentRepo.findByTransportId(transportId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/check-availability/{transportId}/{dateDebut}/{dateFin}")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable String transportId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<TransportAssignment> conflictingAssignments = transportAssignmentRepo.findByTransportIdAndDateRange(
                transportId, dateDebut, dateFin);
        return ResponseEntity.ok(conflictingAssignments.isEmpty());
    }
}
