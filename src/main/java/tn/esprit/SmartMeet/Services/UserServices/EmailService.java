package tn.esprit.SmartMeet.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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


}
