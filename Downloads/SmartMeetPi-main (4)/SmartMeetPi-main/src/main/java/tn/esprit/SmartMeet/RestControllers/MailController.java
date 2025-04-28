package tn.esprit.SmartMeet.RestControllers;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.SmartMeet.DAO.Entities.MailRequest;

import java.io.File;

@RestController
@RequestMapping("/api/mailer")
public class MailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody MailRequest request) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), true); // true = HTML

            // Si tu veux joindre un contrat (PDF déjà généré)
            if (request.getAttachmentPath() != null) {
                FileSystemResource file = new FileSystemResource(new File(request.getAttachmentPath()));
                helper.addAttachment("Contrat.pdf", file);
            }

            mailSender.send(mimeMessage);
            return ResponseEntity.ok("Mail envoyé !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi de l'e-mail");
        }
    }

}
