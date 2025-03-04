package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tn.esprit.SmartMeet.DAO.Entities.Comment;

import java.util.List;

public interface CommentRepository  extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String id);
}
