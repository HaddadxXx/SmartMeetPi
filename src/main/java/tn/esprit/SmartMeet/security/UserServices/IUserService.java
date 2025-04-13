package tn.esprit.SmartMeet.security.UserServices;

import tn.esprit.SmartMeet.models.User;
import java.util.Optional;

public interface IUserService {
    Optional<User> getUserByEmail(String email);
    User updateUserProfile(String email, User updatedUser);
    boolean deleteUserByEmail(String email, String password);

}
