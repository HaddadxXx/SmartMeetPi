package tn.esprit.SmartMeet.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Entities.Comment;
import tn.esprit.SmartMeet.DAO.Entities.Post;
import tn.esprit.SmartMeet.DAO.Repositories.CommentRepository;
import tn.esprit.SmartMeet.DAO.Repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    /*@Autowired
    private PostRepository postRepository;*/

    /*@Autowired
    private CommentRepository commentRepository;

    private final PostRepository postRepository;*/
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Ajouter un post
    public Post addPost(Post newPost) {
        return postRepository.save(newPost);
    }

    // Récupérer tous les posts
    /*public List<Post> getAllPosts() {
        return postRepository.findAll();
    }*/

    /*public List<Post> getAllPosts() {
        return postRepository.findAll();
    }*/

    /*public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            post.setCommentList(comments);
        }
        return posts;
    }*/

    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        System.out.println("Posts trouvés dans la base de données : " + posts);
        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            System.out.println("Commentaires pour le post " + post.getId() + " : " + comments);
            post.setCommentList(comments);
        }
        return posts;
    }

    // Supprimer un post par ID
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    // Ajouter un commentaire à un post
    /*public Post addCommentToPost(String postId, Comment comment) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            commentRepository.save(comment);
            existingPost.getCommentList().add(comment);
            return postRepository.save(existingPost);
        }
        return null;
    }*/
    public Post addCommentToPost(String postId, Comment comment) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            // Vérifie si la liste de commentaires est null
            if (existingPost.getCommentList() == null) {
                existingPost.setCommentList(new ArrayList<>());
            }

            commentRepository.save(comment);
            existingPost.getCommentList().add(comment);
            return postRepository.save(existingPost);
        }
        return null;
    }


    // Récupérer tous les commentaires d'un post
    public List<Comment> getCommentsByPostId(String postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.map(Post::getCommentList).orElse(null);
    }

    public Post updatePost(String id, Post updatedPost) {
        return postRepository.findById(id).map(post -> {
            post.setContent(updatedPost.getContent());
            return postRepository.save(post);
        }).orElseThrow(() -> new RuntimeException("Post non trouvé avec l'ID : " + id));
    }



}
