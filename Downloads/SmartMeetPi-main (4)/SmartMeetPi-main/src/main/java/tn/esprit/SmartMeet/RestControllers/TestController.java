package tn.esprit.SmartMeet.RestControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('SPONSOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

			@GetMapping("/mod")
	@PreAuthorize("hasRole('SPONSOR')")
	public String sponsorAccess() {
		return "Sponsor Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@GetMapping("/protected")
	@PreAuthorize("isAuthenticated()") // Nécessite une authentification
	public String protectedAccess() {
		return "This is a protected resource!";
	}

}
