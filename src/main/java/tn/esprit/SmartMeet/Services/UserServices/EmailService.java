package tn.esprit.SmartMeet.Services.UserServices;
import io.jsonwebtoken.io.IOException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
         // Facultatif
        mailSender.send(message);
    }

    private static final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Génère un code à 6 chiffres
        return String.valueOf(code);
    }

    public void sendVerificationCode(String to) {
        to = to.trim().toLowerCase(); // 🔥 Normalisation

        String code = generateVerificationCode();
        verificationCodes.put(to, code);

        // 🔍 Vérification immédiate de l'enregistrement du code
        System.out.println(" Code enregistré pour [" + to + "] : " + verificationCodes.get(to));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("\uD83D\uDC49 Marhbé biik fi SmartMeet ❤\uFE0F | Your verification code");
        message.setText("Your verification code : " + code);
        mailSender.send(message);
    }


    public boolean verifyCode(String email, String code) {
        email = email.trim().toLowerCase(); // 🔥 Normalisation

        System.out.println(" Vérification pour : [" + email + "]");
        System.out.println(" Emails stockés dans la Map : " + verificationCodes.keySet());

        // 🔥 Vérifier si l'email existe dans la Map
        if (!verificationCodes.containsKey(email)) {
            System.out.println(" Email introuvable !");
            return false;
        }

        System.out.println("*** Code liyestanna fiih : " + verificationCodes.get(email));
        System.out.println("*** Code lijeh : " + code);

        return verificationCodes.get(email).equals(code.trim());
    }
/// /////////
   /* public void envoyerQRCodeParEmail(String destinataire, String codeQRbase64) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(destinataire);
            helper.setSubject("Votre badge numérique");
            helper.setText("<p>Voici votre badge numérique avec code QR :</p><img src='cid:qrcodeImage'>", true);

            byte[] imageBytes = Base64.getDecoder().decode(codeQRbase64.split(",")[1]);
            ByteArrayDataSource imageSource = new ByteArrayDataSource(imageBytes, "image/png");
            helper.addInline("qrcodeImage", imageSource);

            mailSender.send(message);

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }*/
}
