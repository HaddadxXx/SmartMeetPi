package tn.esprit.SmartMeet.Services.Reco;

import java.util.List;
import java.util.Map;

public interface IRecommendationService {
    /**
     * Retourne pour userId la liste des maps contenant :
     *   - "user"  : l'objet User
     *   - "score" : la similarité (double)
     */
    List<Map<String, Object>> recommendFor(String userId);
}