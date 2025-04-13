package tn.esprit.SmartMeet.Services.UserServices;

import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.Friend;
import tn.esprit.SmartMeet.DAO.Entities.FriendRequest;
import tn.esprit.SmartMeet.DAO.Repositories.FriendRepository;
import tn.esprit.SmartMeet.DAO.Repositories.FriendRequestRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendRequestService {

    private final FriendRequestRepository requestRepository;
    private final FriendRepository friendRepository;  // Injection du repository Friend

    public FriendRequestService(FriendRequestRepository requestRepository, FriendRepository friendRepository) {
        this.requestRepository = requestRepository;
        this.friendRepository = friendRepository;
    }

    // Méthode d'envoi d'une demande d'amis (inchangée)
    public FriendRequest sendRequest(String senderId, String receiverId) {
        FriendRequest request = new FriendRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setStatus(FriendRequest.Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    // Méthode de mise à jour du statut de la demande
    public FriendRequest updateStatus(String requestId, FriendRequest.Status newStatus) {
        FriendRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        // Si l'acceptation se fait sur une demande en attente, on crée les enregistrements Friend
        if(request.getStatus() == FriendRequest.Status.PENDING && newStatus == FriendRequest.Status.ACCEPTED) {
            // Création d'une relation bidirectionnelle
            LocalDateTime now = LocalDateTime.now();
            Friend friendForSender = new Friend(request.getSenderId(), request.getReceiverId(), now);
            Friend friendForReceiver = new Friend(request.getReceiverId(), request.getSenderId(), now);
            friendRepository.save(friendForSender);
            friendRepository.save(friendForReceiver);
        }

        request.setStatus(newStatus);
        return requestRepository.save(request);
    }

    public List<FriendRequest> getPendingRequests(String receiverId) {
        return requestRepository.findByReceiverIdAndStatus(receiverId, FriendRequest.Status.PENDING);
    }

    // Récupérer la liste des amis en utilisant les FriendRequest acceptés ou directement via la collection Friend
    public List<Friend> getFriends(String userId) {
        return friendRepository.findByUserId(userId);
    }
}
