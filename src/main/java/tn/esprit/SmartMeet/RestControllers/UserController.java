package tn.esprit.SmartMeet.RestControllers;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.User;
import tn.esprit.SmartMeet.payload.request.DeleteAccountRequest;
import tn.esprit.SmartMeet.Services.UserServices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
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

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestPart("user") User updatedUser,
                                               @RequestPart(value = "file", required = false) MultipartFile file) {
        try {

            User user = userService.updateUserProfile(userDetails.getUsername(), updatedUser, file);
            System.out.println(userDetails);
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


    // Exemple : GET /api/users/search?userId=123&keyword=amine

    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam String userId,
            @RequestParam String keyword
    ) {
        return userService.searchPotentialFriends(userId, keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

}
