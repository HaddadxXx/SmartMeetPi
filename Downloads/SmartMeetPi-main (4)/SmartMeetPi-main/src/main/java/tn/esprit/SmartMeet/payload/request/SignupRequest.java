package tn.esprit.SmartMeet.payload.request;

import java.util.Set;

import tn.esprit.SmartMeet.DAO.Entities.ERole;
import jakarta.validation.constraints.*;
 
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<ERole> roles;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

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
    
    public Set<ERole> getRoles() {
      return this.roles;
    }
    
    public void setRole(Set<ERole> roles) {
      this.roles = roles;
    }
}
