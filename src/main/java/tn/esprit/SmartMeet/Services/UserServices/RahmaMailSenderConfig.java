package tn.esprit.SmartMeet.Services.UserServices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration

public class RahmaMailSenderConfig {

        @Bean(name = "rahmaMailSender")
        public JavaMailSender getRahmaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.office365.com");
            mailSender.setPort(587);
            mailSender.setUsername("rahma.lagha@esprit.tn");
            mailSender.setPassword("dsazrehptflrtkmv "); // ne jamais utiliser le vrai mot de passe Gmail

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            return mailSender;
        }
    }


