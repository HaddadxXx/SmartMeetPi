package tn.esprit.SmartMeet.DAO.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recommendations")
public class UserRecommendation {
    @Id
    private String userId;
    private List<String> recommendedIds;

    public UserRecommendation() {}

    public UserRecommendation(String userId, List<String> recommendedIds) {
        this.userId = userId;
        this.recommendedIds = recommendedIds;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRecommendedIds() {
        return recommendedIds;
    }
    public void setRecommendedIds(List<String> recommendedIds) {
        this.recommendedIds = recommendedIds;
    }
}
