package tn.esprit.SmartMeet.DAO.Entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
  @Id
  private String id;



  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  @NotBlank
  @Size(max = 50)
  @Email
  @Indexed(unique = true)
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  private String profilePicture;
  private String expertiseArea;
  private String interests;
  private double scoreReputation;
  private boolean isVerified;
  @DBRef
  private Set<Role> roles = new HashSet<>();


  private List<String> sentMessages; // Liste des IDs des messages envoyés
  private List<String> receivedMessages; // Liste des IDs des messages reçus

  public List<String> getSentMessages() {
    return sentMessages;
  }

  public void setSentMessages(List<String> sentMessages) {
    this.sentMessages = sentMessages;
  }

  public List<String> getReceivedMessages() {
    return receivedMessages;
  }

  public void setReceivedMessages(List<String> receivedMessages) {
    this.receivedMessages = receivedMessages;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Set<Group> getGroups() {
    return groups;
  }

  public void setGroups(Set<Group> groups) {
    this.groups = groups;
  }

  private String time; // Exemple : "2.40 PM" ou une valeur dynamique
  private String status; // "online", "offline"
  private String message; // Dernier message de l'utilisateur (à dynamiser)
  private Integer count; // Nombre de messages non lus (à dynamiser)






  public User() {
  }

  public User( String firstName, String lastName,String email, String password) {
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }





  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }

  public String getExpertiseArea() {
    return expertiseArea;
  }

  public void setExpertiseArea(String expertiseArea) {
    this.expertiseArea = expertiseArea;
  }

  public String getInterests() {
    return interests;
  }

  public void setInterests(String interests) {
    this.interests = interests;
  }

  public double getScoreReputation() {
    return scoreReputation;
  }

  public void setScoreReputation(double scoreReputation) {
    this.scoreReputation = scoreReputation;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }


  public User(String email, boolean isVerified) {
    this.email=email;
    this.isVerified = isVerified;
  }


  public boolean isVerified() {
    return isVerified;
  }

  public void setVerified(boolean verified) {
    isVerified = verified;
  }




  @DBRef
  private Set<Group> groups = new HashSet<>(); // Liste des groupes où l'utilisateur est membre


  public void joinGroup(Group group) {
    this.groups.add(group);
  }

  public void leaveGroup(Group group) {
    this.groups.remove(group);
  }
}