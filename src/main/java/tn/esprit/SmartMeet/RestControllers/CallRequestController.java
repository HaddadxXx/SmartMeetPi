package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.CallRequest;
import tn.esprit.SmartMeet.Services.Reco.CallRequestService;

@RestController
@RequestMapping("/api/call-requests")
@CrossOrigin(origins = "*", maxAge = 3600)

public class CallRequestController {

    @Autowired
    private CallRequestService callRequestService;

    @PostMapping("/send")
    public ResponseEntity<CallRequest> sendCallRequest(
            @RequestParam String from,
            @RequestParam String to) {
        CallRequest request = callRequestService.sendCallRequest(from, to);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/check-mutual")
    public ResponseEntity<Boolean> checkMutualRequests(
            @RequestParam String user1,
            @RequestParam String user2) {
        boolean haveMutualRequests = callRequestService.haveMutualRequests(user1, user2);
        return ResponseEntity.ok(haveMutualRequests);
    }
}