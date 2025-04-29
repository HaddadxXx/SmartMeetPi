/*package tn.esprit.SmartMeet.Services.Inscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.DAO.Repositories.*;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.Instant;
import java.util.UUID;

@Service
public class InscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(InscriptionService.class);

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepo eventRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${qrcode.width:200}")
    private int qrCodeWidth;

    @Value("${qrcode.height:200}")
    private int qrCodeHeight;

    public Inscription inscrire(String userId, String eventId) throws Exception {
        logger.debug("Processing inscription for userId: {}, eventId: {}", userId, eventId);

        // Fetch user and event, throw exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        // Create and save the inscription
        Inscription inscription = new Inscription();
        inscription.setUser(user);
        inscription.setEvent(event);
        inscription.setDateInscription(Instant.now());

        // Generate an activation code (not stored in Inscription)
        String activationCode = UUID.randomUUID().toString();
        logger.debug("Generated activation code: {}", activationCode);

        Inscription savedInscription = inscriptionRepository.save(inscription);
        logger.debug("Inscription saved successfully: {}", savedInscription);

        // Generate QR code with inscription details, including the activation code
        String qrCodeData = String.format("Inscription ID: %s\nUser: %s\nEvent: %s\nDate: %s\nActivation Code: %s",
                savedInscription.getId(), user.getEmail(), event.getName(), savedInscription.getDateInscription(), activationCode);
        byte[] qrCodeImage = QRCodeUtil.generateQRCode(qrCodeData, qrCodeWidth, qrCodeHeight);
        logger.debug("QR code generated successfully for inscription: {}", savedInscription.getId());

        // Send QR code via email
        sendQRCodeEmail(user.getEmail(), event.getName(), qrCodeImage, activationCode);
        logger.debug("QR code email sent to: {}", user.getEmail());

        return savedInscription;
    }

    private void sendQRCodeEmail(String toEmail, String eventName, byte[] qrCodeImage, String activationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Event Registration QR Code for " + eventName);
        helper.setText("Dear User,\n\nYou have successfully registered for the event: " + eventName +
                ".\nYour activation code is: " + activationCode +
                "\nPlease find your QR code attached below.\n\nBest regards,\nSMART Meet Team");

        // Attach the QR code image using ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(qrCodeImage);
        helper.addAttachment("qrcode.png", resource, "image/png");

        mailSender.send(message);
    }
}*/


//////////////////

package tn.esprit.SmartMeet.Services.Inscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.*;
import tn.esprit.SmartMeet.DAO.Repositories.*;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.Instant;
import java.util.UUID;

@Service
public class InscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(InscriptionService.class);

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepo eventRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${qrcode.width:200}")
    private int qrCodeWidth;

    @Value("${qrcode.height:200}")
    private int qrCodeHeight;

    public Inscription inscrire(String userId, String eventId) throws Exception {
        logger.debug("Processing inscription for userId: {}, eventId: {}", userId, eventId);

        // Fetch user and event, throw exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));

        // Create and save the inscription
        Inscription inscription = new Inscription();
        inscription.setUser(user);
        inscription.setEvent(event);
        inscription.setDateInscription(Instant.now());

        // Generate an activation code (not stored in Inscription)
        String activationCode = UUID.randomUUID().toString();
        logger.debug("Generated activation code: {}", activationCode);

        Inscription savedInscription = inscriptionRepository.save(inscription);
        logger.debug("Inscription saved successfully: {}", savedInscription);

        // Generate QR code with inscription details, including the activation code
        String qrCodeData = String.format("Inscription ID: %s\nUser: %s\nEvent: %s\nDate: %s\nActivation Code: %s",
                savedInscription.getId(), user.getEmail(), event.getNomEvent(), savedInscription.getDateInscription(), activationCode);
        byte[] qrCodeImage = QRCodeUtil.generateQRCode(qrCodeData, qrCodeWidth, qrCodeHeight);
        logger.debug("QR code generated successfully for inscription: {}", savedInscription.getId());

        // Send QR code via email
        sendQRCodeEmail(user.getEmail(), event.getNomEvent(), qrCodeImage, activationCode);
        logger.debug("QR code email sent to: {}", user.getEmail());

        return savedInscription;
    }

    public byte[] getQRCode(String inscriptionId) throws Exception {
        logger.debug("Generating QR code for inscriptionId: {}", inscriptionId);

        // Fetch the inscription
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscription not found with ID: " + inscriptionId));

        // Generate an activation code (since it's not stored)
        String activationCode = UUID.randomUUID().toString();
        logger.debug("Generated activation code for QR code: {}", activationCode);

        // Generate QR code with inscription details
        String qrCodeData = String.format("Inscription ID: %s\nUser: %s\nEvent: %s\nDate: %s\nActivation Code: %s",
                inscription.getId(), inscription.getUser().getEmail(), inscription.getEvent().getNomEvent(),
                inscription.getDateInscription(), activationCode);
        return QRCodeUtil.generateQRCode(qrCodeData, qrCodeWidth, qrCodeHeight);
    }

    private void sendQRCodeEmail(String toEmail, String eventName, byte[] qrCodeImage, String activationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Event Registration QR Code for " + eventName);
        helper.setText("Dear User,\n\nYou have successfully registered for the event: " + eventName +
                ".\nYour activation code is: " + activationCode +
                "\nPlease find your QR code attached below.\n\nBest regards,\nSMART Meet Team");

        ByteArrayResource resource = new ByteArrayResource(qrCodeImage);
        helper.addAttachment("qrcode.png", resource, "image/png");

        mailSender.send(message);
    }
    public Inscription verifyInscription(String inscriptionId) {
        logger.debug("Verifying inscription with ID: {}", inscriptionId);
        return inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscription not found with ID: " + inscriptionId));
    }
}