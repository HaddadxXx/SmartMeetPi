package tn.esprit.SmartMeet.Services.Reco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.CallRequest;
import tn.esprit.SmartMeet.DAO.Repositories.CallRequestRepository;


import java.util.List;




@Service
public class CallRequestService {
    @Autowired
    private CallRequestRepository repo;

    public CallRequest sendCallRequest(String from, String to) {
        List<CallRequest> existingRequests = repo.findByFromAndTo(from, to);
        if (existingRequests.isEmpty()) {
            CallRequest request = new CallRequest();
            request.setFrom(from);
            request.setTo(to);
            return repo.save(request);
        }
        return existingRequests.get(0);
    }

    public boolean haveMutualRequests(String user1, String user2) {
        List<CallRequest> requestFromUser1ToUser2 = repo.findByFromAndTo(user1, user2);
        List<CallRequest> requestFromUser2ToUser1 = repo.findByFromAndTo(user2, user1);
        return !requestFromUser1ToUser2.isEmpty() && !requestFromUser2ToUser1.isEmpty();
    }
}

