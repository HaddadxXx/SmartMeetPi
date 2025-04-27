package tn.esprit.SmartMeet.RestControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Message;
import tn.esprit.SmartMeet.Services.UserServices.IMessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*", maxAge = 3600)

public class MessageController {
    private final IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{conversationId}")
    public List<Message> getMessages(@PathVariable String conversationId) {
        return messageService.getMessagesByConversationId(conversationId);
    }

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }
}