package tn.esprit.SmartMeet.Schedular;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.SmartMeet.DAO.Entities.User;
import tn.esprit.SmartMeet.DAO.Entities.UserRecommendation;
import tn.esprit.SmartMeet.DAO.Repositories.UserRecommendationRepository;
import tn.esprit.SmartMeet.DAO.Repositories.UserRepository;
import tn.esprit.SmartMeet.Services.Reco.IRecommendationService;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RecommendationScheduler {

    private final UserRepository userRepository;
    private final IRecommendationService recommendationService;
    private final UserRecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationScheduler(UserRepository userRepository,
                                   IRecommendationService recommendationService,
                                   UserRecommendationRepository recommendationRepository) {
        this.userRepository = userRepository;
        this.recommendationService = recommendationService;
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * Toutes les heures (cron), tu peux adapter la fréquence.
     * Ici : à chaque début d'heure : 0 0 * * * *
     */
    @Scheduled(fixedRate = 60_000)
    public void computeAndSaveAllRecommendations() {
        List<User> users = userRepository.findAll();

        for (User u : users) {
            // récupère List<Map<"user", User>, "score", Double>
            List<Map<String,Object>> recos = recommendationService.recommendFor(u.getId());

            // extrait les 5 userIds
            List<String> top5Ids = recos.stream()
                    .map(m -> ((User) m.get("user")).getId())
                    .distinct()               // pour supprimer d’éventuels doublons
                    .limit(5)                 // ne prendre que les 5 premiers
                    .collect(Collectors.toList());


            // construit et sauve (upsert)
            UserRecommendation doc = new UserRecommendation(u.getId(), top5Ids);
            recommendationRepository.save(doc);
        }
    }
}
