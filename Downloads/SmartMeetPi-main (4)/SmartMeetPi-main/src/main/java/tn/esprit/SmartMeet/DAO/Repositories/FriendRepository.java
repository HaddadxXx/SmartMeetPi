package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.SmartMeet.DAO.Entities.Friend;

import java.util.List;

public interface FriendRepository extends MongoRepository<Friend, String> {
    List<Friend> findByUserId(String userId);
    boolean existsByUserIdAndFriendId(String userId, String friendId);

}

