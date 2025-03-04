package tn.esprit.SmartMeet.DAO.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "post")
public class Post {
    @Id
    private String id;
    private String content;

    public Post(String content) {
        //this.id = id;
        this.content = content;
    }

    public Post() {}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private TypeReact react;

    private List<Comment> commentList;

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public TypeReact getReact() {
        return react;
    }

    public void setReact(TypeReact react) {
        this.react = (react != null) ? react : TypeReact.RIEN; // Assigner RIEN si null
    }
}
