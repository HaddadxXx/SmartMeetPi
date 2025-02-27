package tn.esprit.SmartMeet.RestControllers;

import tn.esprit.SmartMeet.DAO.Entities.User;
import tn.esprit.SmartMeet.payload.request.DeleteAccountRequest;
import tn.esprit.SmartMeet.Services.UserServices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userService.getUserByEmail(userDetails.getUsername());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody User updatedUser) {
        try {
            User user = userService.updateUserProfile(userDetails.getUsername(), updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody DeleteAccountRequest request) {
        boolean isDeleted = userService.deleteUserByEmail(userDetails.getUsername(), request.getPassword());
        if (isDeleted) {
            return ResponseEntity.ok().body("User deleted successfully.");
        }
        return ResponseEntity.badRequest().body("Invalid password.");
    }
}
