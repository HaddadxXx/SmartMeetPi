package tn.esprit.SmartMeet.Services.UserServices;


import tn.esprit.SmartMeet.DAO.Entities.Message;

import java.util.List;

public interface IMessageService {

    List<Message> getMessagesByConversationId(String conversationId);
    Message sendMessage(Message message);

}

