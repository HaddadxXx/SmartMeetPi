package tn.esprit.SmartMeet.Services.UserServices;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserById(String id);

    User updateUserProfile(String email, User updatedUser, MultipartFile file);

    boolean deleteUserByEmail(String email, String password);
    public List<User> searchPotentialFriends(String userId, String keyword);

    public List<User> getAllUsers();
}
