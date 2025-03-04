package tn.esprit.SmartMeet.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.Comment;
import tn.esprit.SmartMeet.DAO.Repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // Ajouter un commentaire
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // Récupérer tous les commentaires
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // Supprimer un commentaire par ID
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    // Modifier un commentaire
    public Comment updateComment(String id, Comment updatedComment) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment existingComment = optionalComment.get();
            existingComment.setText(updatedComment.getText());
            return commentRepository.save(existingComment);
        }
        return null;
    }
}
