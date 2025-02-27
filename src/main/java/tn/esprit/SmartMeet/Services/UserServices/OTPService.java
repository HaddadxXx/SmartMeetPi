package tn.esprit.SmartMeet.Services.UserServices;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class OTPService {




    private final Map<String, String> otpCache = new HashMap<>();  // Stockage temporaire des OTPs

    @Autowired
    private JavaMailSender emailSender;

    // Générer un OTP aléatoire à 6 chiffres
    public String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Envoyer l'OTP par email
    public void sendOTP(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for SMART Meet");
        message.setText("Your OTP is: " + otp);
        emailSender.send(message);
    }

    // Stocker l'OTP pour validation
    public void storeOTP(String email, String otp) {
        otpCache.put(email, otp);
    }

    // Vérifier si l'OTP est correct
    public boolean validateOTP(String email, String otp) {
        return otpCache.containsKey(email) && otpCache.get(email).equals(otp);
    }

    // Supprimer un OTP après validation
    public void removeOTP(String email) {
        otpCache.remove(email);
    }
    }
