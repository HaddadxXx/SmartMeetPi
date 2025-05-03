package tn.esprit.SmartMeet.RestControllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.messaging.handler.annotation.MessageMapping;
import tn.esprit.SmartMeet.DAO.Entities.Message;
import tn.esprit.SmartMeet.Services.UserServices.IMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class WebSocketController {
    @Autowired
    private IMessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(IMessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(Message message) {
        // Sauvegarder le message dans la base de données
        Message savedMessage = messageService.sendMessage(message);
        // Diffuser le message aux abonnés du topic spécifique à la conversation
        messagingTemplate.convertAndSend("/topic/messages/" + message.getConversationId(), savedMessage);
    }
}