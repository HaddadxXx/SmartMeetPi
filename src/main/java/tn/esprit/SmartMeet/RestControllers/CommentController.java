package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Comment;
import tn.esprit.SmartMeet.Services.CommentService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    // Ajouter un commentaire
    @PostMapping("/add")
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    // Récupérer tous les commentaires
    @GetMapping("/all")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    // Supprimer un commentaire par ID
    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
    }

    // Modifier un commentaire
    @PutMapping("/update/{id}")
    public Comment updateComment(@PathVariable String id, @RequestBody Comment updatedComment) {
        return commentService.updateComment(id, updatedComment);
    }
}
