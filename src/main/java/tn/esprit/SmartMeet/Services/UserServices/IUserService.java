package tn.esprit.SmartMeet.Services.UserServices;

import tn.esprit.SmartMeet.DAO.Entities.User;
import java.util.Optional;

public interface IUserService {
    Optional<User> getUserByEmail(String email);
    User updateUserProfile(String email, User updatedUser);
    boolean deleteUserByEmail(String email, String password);

}
