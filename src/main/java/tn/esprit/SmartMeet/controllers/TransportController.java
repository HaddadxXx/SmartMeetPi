package tn.esprit.SmartMeet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.models.Transport;
import tn.esprit.SmartMeet.services.TransportService;

import java.util.List;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/transports")
public class TransportController {


    @Autowired
    private TransportService transportService;

    @GetMapping("/all")
    public List<Transport> getAllTransports() {
        return transportService.getAllTransports();
    }
    @GetMapping("/{id}")
    public Optional<Transport> getTransportById(@PathVariable String id) {
        return transportService.getTransportById(id);
    }

    @PostMapping("/create")
    public Transport createTransport(@RequestBody Transport transport) {
        return transportService.createTransport(transport);
    }

    @PutMapping("/update/{id}")
    public Transport updateTransport(@PathVariable String id, @RequestBody Transport transport) {
        return transportService.updateTransport(id, transport);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTransport(@PathVariable String id) {
        transportService.deleteTransport(id);
    }

}
