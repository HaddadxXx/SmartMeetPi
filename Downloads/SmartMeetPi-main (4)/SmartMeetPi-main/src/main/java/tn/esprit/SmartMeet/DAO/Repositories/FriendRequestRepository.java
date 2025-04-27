package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.SmartMeet.DAO.Entities.FriendRequest;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findByReceiverIdAndStatus(String receiverId, FriendRequest.Status status);
    List<FriendRequest> findBySenderId(String senderId);
    List<FriendRequest> findBySenderIdAndStatus(String senderId, FriendRequest.Status status);
    List<FriendRequest> findByStatusAndReceiverIdOrSenderId(FriendRequest.Status status, String receiverId, String senderId);
}
