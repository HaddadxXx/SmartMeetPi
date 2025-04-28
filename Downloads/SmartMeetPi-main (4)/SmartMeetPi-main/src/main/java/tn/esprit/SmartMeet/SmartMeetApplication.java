package tn.esprit.SmartMeet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling

public class SmartMeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartMeetApplication.class, args);
	}

}
