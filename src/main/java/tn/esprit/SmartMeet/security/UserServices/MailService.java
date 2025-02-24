package tn.esprit.SmartMeet.security.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOTPEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Votre code de vérification SmartMeet");
        message.setText("Votre code OTP est : " + otp + "\nNe partagez ce code avec personne.");

        mailSender.send(message);
    }
}
