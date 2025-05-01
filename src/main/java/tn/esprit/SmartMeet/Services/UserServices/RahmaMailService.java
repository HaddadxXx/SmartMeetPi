package tn.esprit.SmartMeet.Services.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class RahmaMailService {
    @Autowired
    @Qualifier("rahmaMailSender")
    private JavaMailSender mailSender;

    public void envoyerMail(String to, String sujet, String corps) {
        try {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("rahma.lagha@esprit.tn"); // <--- AJOUT OBLIGATOIRE
        message.setTo(to);
        message.setSubject(sujet);
        message.setText(corps);
        mailSender.send(message);
            System.out.println("✅ Email envoyé à " + to + " avec sujet : " + sujet);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l’envoi de l’email : " + e.getMessage());
            e.printStackTrace();
        }

    }
}
