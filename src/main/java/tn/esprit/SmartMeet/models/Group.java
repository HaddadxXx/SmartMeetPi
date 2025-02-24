package tn.esprit.SmartMeet.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.List;


@Document(collection = "groups")
public class Group {

    @Id
    private String id;

    private String name;
    private String description;
    private String visibility; // PUBLIC, PRIVATE
    private String photo; // URL de l'image du groupe
    private LocalDateTime createdAt = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    @DBRef
    private User owner; // Propriétaire du groupe

    @DBRef
    private Set<User> members = new HashSet<>();

    //@DBRef
   // private List<Post> posts; // Liste des publications dans le groupe

    // Constructeurs
    public Group() {}

    public Group(String name, String description, String visibility, String photo, User owner) {
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.photo = photo;
        this.owner = owner;
        this.createdAt = LocalDateTime.now();
    }

    // Ajouter un membre
    public void addMember(User user) {
        this.members.add(user);
    }

    // Supprimer un membre
    public void removeMember(User user) {
        this.members.remove(user);
    }



}