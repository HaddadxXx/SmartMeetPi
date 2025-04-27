package tn.esprit.SmartMeet.Services.UserServices;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.Friend;
import tn.esprit.SmartMeet.DAO.Entities.User;
import tn.esprit.SmartMeet.DAO.Repositories.FriendRepository;
import tn.esprit.SmartMeet.DAO.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    private final String UPLOAD_DIRECTORY = "uploads/";

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    public String uploadPhoto(MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIRECTORY + fileName);
                Files.copy(file.getInputStream(), path);
                return fileName; // Retourne le chemin du fichier
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUserProfile(String email, User updatedUser,MultipartFile file) {
        return userRepository.findByEmail(email).map(existingUser -> {
            if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isEmpty()) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty()) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            // 🔁 Upload de l'image si elle est fournie
            if (file != null && !file.isEmpty()) {
                String fileName = uploadPhoto(file);
                if (fileName != null) {
                    existingUser.setProfilePicture(fileName);
                }

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



    /////////////////////////
    // Récupère tous les utilisateurs qui ne sont pas encore amis avec l'utilisateur actuel
    public List<User> searchPotentialFriends(String userId, String keyword) {
        List<Friend> existingFriends = friendRepository.findByUserId(userId);
        Set<String> excludedIds = existingFriends.stream()
                .map(Friend::getFriendId)
                .collect(Collectors.toSet());
        excludedIds.add(userId);

        List<User> matchingUsers = userRepository.searchByEmailOrFirstNameOrLastName(keyword);

        return matchingUsers.stream()
                .filter(user -> !excludedIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}

