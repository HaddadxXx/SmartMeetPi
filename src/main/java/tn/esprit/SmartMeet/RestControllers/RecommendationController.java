package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Repositories.UserRecommendationRepository;
import tn.esprit.SmartMeet.Services.Reco.IRecommendationService;

import tn.esprit.SmartMeet.DAO.Entities.UserRecommendation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RecommendationController {

    private final IRecommendationService recommendationService;
    private final UserRecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationController(IRecommendationService recommendationService, UserRecommendationRepository recommendationRepository) {
        this.recommendationService = recommendationService;
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * Lit **uniquement** les 5 IDs déjà stockés par le scheduler
     */
    @GetMapping("/recommendations/ids/{userId}")
    public ResponseEntity<List<String>> getStoredRecommendationIds(@PathVariable String userId) {
        return recommendationRepository.findById(userId)
                .map(UserRecommendation::getRecommendedIds)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getRecommendations(@PathVariable String userId) {
        List<Map<String, Object>> recos = recommendationService.recommendFor(userId);
        return ResponseEntity.ok(recos);
    }




}