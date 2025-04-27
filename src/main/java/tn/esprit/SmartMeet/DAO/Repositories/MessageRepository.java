package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.SmartMeet.DAO.Entities.Message;
import tn.esprit.SmartMeet.DAO.Entities.Group;
import tn.esprit.SmartMeet.DAO.Entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByConversationIdOrderByTimestampAsc(String conversationId);
}