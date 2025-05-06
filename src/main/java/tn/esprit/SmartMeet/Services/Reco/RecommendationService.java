package tn.esprit.SmartMeet.Services.Reco;
// src/main/java/tn/esprit/SmartMeet/services/RecommendationServiceImpl.java


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.SmartMeet.DAO.Entities.User;
import tn.esprit.SmartMeet.DAO.Repositories.UserRepository;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService implements IRecommendationService {

    private static final String FLASK_URL = "http://localhost:5000/match";

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public RecommendationService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Map<String, Object>> recommendFor(String currentUserId) {
        // 1. Charger tous les utilisateurs
        List<User> all = userRepository.findAll();

        // 2. Isoler l'utilisateur courant et les candidats
        User current = all.stream()
                .filter(u -> u.getId().equals(currentUserId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        List<User> candidates = all.stream()
                .filter(u -> !u.getId().equals(currentUserId))
                .collect(Collectors.toList());

        // 3. Préparer le payload
        String profile = formatProfile(current);
        List<String> texts = candidates.stream()
                .map(this::formatProfile)
                .collect(Collectors.toList());
        Map<String, Object> payload = new HashMap<>();
        payload.put("profile", profile);
        payload.put("candidates", texts);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers);

        // 4. Appel à l’API Flask
        ResponseEntity<List> resp = restTemplate.exchange(
                FLASK_URL, HttpMethod.POST, req, List.class
        );

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> raw = resp.getBody();

        // 5. Concaténer User + score
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : raw) {
            double score = Double.parseDouble(item.get("score").toString());
            String candText = (String) item.get("candidate");

            candidates.stream()
                    .filter(u -> formatProfile(u).equals(candText))
                    .findFirst()
                    .ifPresent(u -> {
                        Map<String, Object> entry = new HashMap<>();
                        entry.put("user", u);
                        entry.put("score", score);
                        result.add(entry);
                    });
        }
        return result;
    }

    private String formatProfile(User u) {
        return String.format(
                "About me: %s. Interests: %s. Expertise: %s",
                u.getAboutMe(), u.getInterests(), u.getExpertiseArea()
        );
    }
}
