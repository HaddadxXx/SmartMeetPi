package tn.esprit.SmartMeet.RestControllers;

import java.util.*;
import java.util.stream.Collectors;

import tn.esprit.SmartMeet.models.BlacklistedToken;
import tn.esprit.SmartMeet.Repositories.BlacklistedTokenRepository;
import tn.esprit.SmartMeet.security.UserServices.EmailService;
import tn.esprit.SmartMeet.security.UserServices.OTPService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import tn.esprit.SmartMeet.models.ERole;
import tn.esprit.SmartMeet.models.Role;
import tn.esprit.SmartMeet.models.User;
import tn.esprit.SmartMeet.payload.request.LoginRequest;
import tn.esprit.SmartMeet.payload.request.SignupRequest;
import tn.esprit.SmartMeet.payload.response.JwtResponse;
import tn.esprit.SmartMeet.payload.response.MessageResponse;
import tn.esprit.SmartMeet.Repositories.RoleRepository;
import tn.esprit.SmartMeet.Repositories.UserRepository;
import tn.esprit.SmartMeet.security.jwt.JwtUtils;
import tn.esprit.SmartMeet.security.UserServices.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private OTPService otpService;

	@Autowired
	private EmailService emailService;



	private final Map<String, User> tempUserStorage = new HashMap<>();

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		System.out.println("yaaaaaaaaaaaaaaaaaaaaa");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(),
												 userDetails.getUsername(),
												 //user
												 userDetails.getEmail(),
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		System.out.println("--------------------------");
		System.out.println("yaaaaaaaaaaaaaaaaaaaaa");

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {

			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getFirstName(),
				             signUpRequest.getLastName(),
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<ERole> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();
		System.out.println("dddddddddd");
		System.out.println(strRoles);
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);

		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case ROLE_ADMIN:
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
					case ROLE_SPONSOR:
					Role modRole = roleRepository.findByName(ERole.ROLE_SPONSOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});

		}

		user.setRoles(roles);
		// Stocker temporairement l'utilisateur avant la vérification
		tempUserStorage.put(signUpRequest.getEmail(), user);

		// Envoyer le code de vérification
		emailService.sendVerificationCode(user.getEmail());




		return ResponseEntity.ok(new MessageResponse("User registered successfully!! Please verify your email with the code sent"));
	}

	private final BlacklistedTokenRepository blacklistedTokenRepository;

	public AuthController(BlacklistedTokenRepository blacklistedTokenRepository) {
		this.blacklistedTokenRepository = blacklistedTokenRepository;
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokenHeader) {
		if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Token invalide");
		}

		String token = tokenHeader.substring(7);
		BlacklistedToken blacklistedToken = new BlacklistedToken();
		blacklistedToken.setToken(token);
		blacklistedToken.setExpirationDate(new Date(System.currentTimeMillis() + 3600000)); // Expiration après 1h

		blacklistedTokenRepository.save(blacklistedToken);

		SecurityContextHolder.clearContext(); // Supprimer les informations d'authentification en mémoire
		return ResponseEntity.ok("Déconnexion réussie");

	}



	@PostMapping("/send")
	public String sendEmail(
			@RequestParam String to,
			@RequestParam String subject,
			@RequestParam String body) {
		emailService.sendEmail(to, subject, body);
		return "Email envoyé avec succès !";
	}


	@PostMapping("/send-code")
	public String sendVerificationCode(@RequestParam String to) {
		emailService.sendVerificationCode(to);
		return "Code de vérification envoyé à " + to;
	}

	@GetMapping("/verify-code")
	public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
		boolean isValid = emailService.verifyCode(email, code);
		System.out.println("Utilisateurs stockés temporairement : " + tempUserStorage);


		if (!isValid) {
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid verification code!"));
		}

		// Vérifier si l'utilisateur existe dans le stockage temporaire
		User user = tempUserStorage.get(email);
		if (user == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: No pending registration found for this email."));
		}

		// Sauvegarder l'utilisateur dans la base de données
		userRepository.save(user);

		// Supprimer l'utilisateur du stockage temporaire après l'enregistrement
		tempUserStorage.remove(email);

		return ResponseEntity.ok(new MessageResponse("Email verified successfully! You can now log in."));
	}








}
