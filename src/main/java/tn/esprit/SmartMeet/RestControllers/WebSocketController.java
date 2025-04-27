package tn.esprit.SmartMeet.RestControllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.messaging.handler.annotation.MessageMapping;
import tn.esprit.SmartMeet.DAO.Entities.Message;
import tn.esprit.SmartMeet.Services.UserServices.IMessageService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class WebSocketController {
    @Autowired
    private IMessageService messageService;

}