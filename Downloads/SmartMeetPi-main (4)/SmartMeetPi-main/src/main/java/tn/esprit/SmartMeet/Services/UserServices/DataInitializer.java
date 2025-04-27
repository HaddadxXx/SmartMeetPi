package tn.esprit.SmartMeet.Services.UserServices;

import tn.esprit.SmartMeet.DAO.Entities.User;
import tn.esprit.SmartMeet.DAO.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import tn.esprit.SmartMeet.DAO.Entities.ERole;
import tn.esprit.SmartMeet.DAO.Entities.Role;
import tn.esprit.SmartMeet.DAO.Repositories.RoleRepository;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Ensure roles exist
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_ADMIN)));

        roleRepository.findByName(ERole.ROLE_USER)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_USER)));

        roleRepository.findByName(ERole.ROLE_SPONSOR)
                .orElseGet(() -> roleRepository.save(new Role(ERole.ROLE_SPONSOR)));

        // Ensure admin user exists
        Optional<User> existingAdmin = userRepository.findByEmail("mohamedkhalil.haddad@esprit.tn");

        if (existingAdmin.isEmpty()) {
            User adminUser = new User();
            adminUser.setFirstName("Mohamed");
            adminUser.setLastName("Haddad");
            adminUser.setEmail("mohamedkhalil.haddad@esprit.tn");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Encrypt password
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            adminUser.setRoles(roles);

            userRepository.save(adminUser);
            System.out.println("✅ Admin user created successfully!");
        } else {
            System.out.println("⚠️ Admin user already exists. Skipping creation.");
        }
    }
}

