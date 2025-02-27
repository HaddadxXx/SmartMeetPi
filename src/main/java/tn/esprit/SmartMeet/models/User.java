package tn.esprit.SmartMeet.models;

import java.util.HashSet;
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
}
