package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.DAO.Entities.Comment;
import tn.esprit.SmartMeet.DAO.Entities.Post;
import tn.esprit.SmartMeet.Services.PostService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/posts")
public class PostController {

    /*@Autowired
    private PostService postService;*/
    @Autowired
    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }



    // Ajouter un post
    @PostMapping("/add")
    public Post addPost(@RequestBody Post newPost) {
        return postService.addPost(newPost);
    }

    // Récupérer tous les posts
    /*@GetMapping("/all")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }*/

    /*@GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        System.out.println("Posts récupérés : " + posts);
        return ResponseEntity.ok(posts);
    }*/

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        System.out.println("📌 Nombre de posts trouvés : " + posts.size());
        for (Post post : posts) {
            System.out.println("➡️ Post : " + post.getId() + " | " + post.getContent());
        }
        return ResponseEntity.ok(posts);
    }


    // Supprimer un post par ID
    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable String id) {
        postService.deletePost(id);
    }

    // Ajouter un commentaire à un post
    /*@PostMapping("/{postId}/comment")
    public Post addCommentToPost(@PathVariable String postId, @RequestBody Comment comment) {
        return postService.addCommentToPost(postId, comment);
    }*/

    @PostMapping("/{postId}/comment")
    public ResponseEntity<Post> addCommentToPost(@PathVariable String postId, @RequestBody Comment comment) {
        Post updatedPost = postService.addCommentToPost(postId, comment);
        if (updatedPost != null) {
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Récupérer tous les commentaires d'un post
    @GetMapping("/{postId}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable String postId) {
        return postService.getCommentsByPostId(postId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable String id, @RequestBody Post updatedPost) {
        try {
            return ResponseEntity.ok(postService.updatePost(id, updatedPost));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
