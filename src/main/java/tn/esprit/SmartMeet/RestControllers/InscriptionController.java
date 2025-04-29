/*package tn.esprit.SmartMeet.RestControllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Inscription;
import tn.esprit.SmartMeet.Services.Inscription.InscriptionService;


@RestController
@RequestMapping("/api/inscriptions")
@CrossOrigin(origins = "http://localhost:4200")
public class InscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(InscriptionController.class);

    @Autowired
    private InscriptionService inscriptionService;

    public static class InscriptionRequest {
        private String userId;
        private String eventId;

        // Getters and setters
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }
    }

    @PostMapping
    public ResponseEntity<Inscription> inscrire(@Valid @RequestBody InscriptionRequest request) {
        logger.debug("Received request to create inscription for userId: {}, eventId: {}",
                request.getUserId(), request.getEventId());

        try {
            Inscription inscription = inscriptionService.inscrire(request.getUserId(), request.getEventId());
            return ResponseEntity.ok(inscription);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create inscription: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Unexpected error while creating inscription", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}*/
/////////////////////////////

package tn.esprit.SmartMeet.RestControllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Inscription;
import tn.esprit.SmartMeet.Services.Inscription.InscriptionService;


@RestController
@RequestMapping("/api/inscriptions")
@CrossOrigin(origins = "http://localhost:4200")
public class InscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(InscriptionController.class);

    @Autowired
    private InscriptionService inscriptionService;

    public static class InscriptionRequest {
        private String userId;
        private String eventId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }
    }

    @PostMapping
    public ResponseEntity<Inscription> inscrire(@Valid @RequestBody InscriptionRequest request) {
        logger.debug("Received request to create inscription for userId: {}, eventId: {}",
                request.getUserId(), request.getEventId());

        try {
            Inscription inscription = inscriptionService.inscrire(request.getUserId(), request.getEventId());
            return ResponseEntity.ok(inscription);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create inscription: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Unexpected error while creating inscription", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<byte[]> getQRCode(@PathVariable String id) {
        logger.debug("Received request to fetch QR code for inscriptionId: {}", id);

        try {
            byte[] qrCodeImage = inscriptionService.getQRCode(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCodeImage);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to fetch QR code: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching QR code", e);
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/verify/{id}")
    public ResponseEntity<Inscription> verifyInscription(@PathVariable String id) {
        logger.debug("Received request to verify inscriptionId: {}", id);

        try {
            Inscription inscription = inscriptionService.verifyInscription(id);
            return ResponseEntity.ok(inscription);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to verify inscription: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("Unexpected error while verifying inscription", e);
            return ResponseEntity.status(500).body(null);
        }
    }
}