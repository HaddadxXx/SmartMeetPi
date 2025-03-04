package tn.esprit.SmartMeet.security.UserServices;

import org.springframework.security.core.context.SecurityContextHolder;
import tn.esprit.SmartMeet.models.User;
import tn.esprit.SmartMeet.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

   // @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    // Récupérer l'utilisateur connecté
    public User getCurrentAuthenticatedUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Récupérer l'email de l'utilisateur à partir du principal
        Optional<User> userOpt = userRepository.findByEmail(principal.getUsername());

        return userOpt.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUserProfile(String email, User updatedUser) {
        return userRepository.findByEmail(email).map(existingUser -> {
            if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isEmpty()) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty()) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getProfilePicture() != null && !updatedUser.getProfilePicture().isEmpty()) {
                existingUser.setProfilePicture(updatedUser.getProfilePicture());
            }
            if (updatedUser.getExpertiseArea() != null && !updatedUser.getExpertiseArea().isEmpty()) {
                existingUser.setExpertiseArea(updatedUser.getExpertiseArea());
            }
            if (updatedUser.getInterests() != null && !updatedUser.getInterests().isEmpty()) {
                existingUser.setInterests(updatedUser.getInterests());
            }
            // Ne pas modifier `scoreReputation`

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public boolean deleteUserByEmail(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }
}
