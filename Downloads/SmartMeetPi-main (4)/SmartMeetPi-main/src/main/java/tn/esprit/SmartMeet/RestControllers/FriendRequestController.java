package tn.esprit.SmartMeet.RestControllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Friend;
import tn.esprit.SmartMeet.DAO.Entities.FriendRequest;
import tn.esprit.SmartMeet.Services.UserServices.FriendRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {
    private final FriendRequestService service;

    public FriendRequestController(FriendRequestService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public FriendRequest sendRequest(@RequestParam String senderId, @RequestParam String receiverId) {
        return service.sendRequest(senderId, receiverId);
    }

    @PostMapping("/respond")
    public FriendRequest respond(@RequestParam String requestId, @RequestParam String status) {
        return service.updateStatus(requestId, FriendRequest.Status.valueOf(status));
    }

    @GetMapping("/pending/{receiverId}")
    public List<FriendRequest> getPending(@PathVariable String receiverId) {
        return service.getPendingRequests(receiverId);
    }

    @GetMapping("/friends/{userId}")
    public List<Friend> getFriends(@PathVariable String userId) {
        return service.getFriends(userId);
    }
}
