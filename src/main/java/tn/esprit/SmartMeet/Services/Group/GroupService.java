package tn.esprit.SmartMeet.Services.Group;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Repositories.GroupRepository;
import tn.esprit.SmartMeet.DAO.Repositories.UserRepository;
import tn.esprit.SmartMeet.DAO.Entities.Group;
import tn.esprit.SmartMeet.DAO.Entities.User;
import org.springframework.security.core.Authentication;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import java.util.List;
import java.util.logging.Logger;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    private final String UPLOAD_DIRECTORY = "uploads/";

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

    /*
    public Group createGroup(Group group, MultipartFile file) {


        // Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        // Extraire l'email depuis UserDetails
        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // getUsername() retourne l'email
        } else {
            throw new RuntimeException("Impossible de récupérer l'email de l'utilisateur");
        }

        // Chercher l'utilisateur en base de données
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Définir l'utilisateur comme propriétaire du groupe
        group.setOwner(owner);

        // Ajouter l'utilisateur à la liste des membres
        if (group.getMembers() == null) {
            group.setMembers(new HashSet<>());
        }
        group.getMembers().add(owner);



        if (file != null) {
            String photoPath = uploadPhoto(file);
            if (photoPath != null) {
                group.setPhoto(photoPath);
            }
        }
        return groupRepository.save(group);
    }
*/

    /************************/

    public Group createGroup(Group group, MultipartFile file) {

        // Auth + user loading logic unchanged...
// Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        // Extraire l'email depuis UserDetails
        String email;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // getUsername() retourne l'email
        } else {
            throw new RuntimeException("Impossible de récupérer l'email de l'utilisateur");
        }

        // Chercher l'utilisateur en base de données
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si le groupe est lié à la science
        if (!isRelatedToScience(group.getName(), group.getDescription())) {
            throw new RuntimeException("Le groupe n'est pas lié au domaine scientifique.");
        }

        // Set owner, add to members, handle photo (same as before)
        group.setOwner(owner);
        if (group.getMembers() == null) {
            group.setMembers(new HashSet<>());
        }
        group.getMembers().add(owner);

        if (file != null) {
            String photoPath = uploadPhoto(file);
            if (photoPath != null) {
                group.setPhoto(photoPath);
            }
        }

        return groupRepository.save(group);
    }

    private boolean isRelatedToScience(String name, String description) {
        String prompt = String.format(
                "Is the following group name and description related to the science domain?\n\nName: %s\nDescription: %s\n\nAnswer only YES or NO.",
                name, description
        );

        String apiKey = "AIzaSyDbUoKYMNChLdK-jmcwF64FSjWOZc-yaec";
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt))
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            Map content = (Map) ((List) response.getBody().get("candidates")).get(0);
            Map innerContent = (Map) content.get("content");
            List parts = (List) innerContent.get("parts");
            String answer = ((Map) parts.get(0)).get("text").toString().trim().toLowerCase();

            return answer.contains("yes");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification avec Gemini: " + e.getMessage());
        }
    }

    /*******************/

    @Override
    public Group getGroupById(String groupId) {
        return groupRepository.findById(groupId).orElse(null);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group updateGroup(String groupId, Group updatedGroup) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setName(updatedGroup.getName());
            group.setDescription(updatedGroup.getDescription());
            group.setVisibility(updatedGroup.getVisibility());
            group.setPhoto(updatedGroup.getPhoto());
            return groupRepository.save(group);
        }
        return null;
    }

    @Override
    public void deleteGroup(String groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public String addMemberToGroup(String groupId, String userId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalGroup.isPresent() && optionalUser.isPresent()) {
            Group group = optionalGroup.get();
            User user = optionalUser.get();

            if (!group.getMembers().contains(user)) {
                group.addMember(user);
                user.joinGroup(group);
                groupRepository.save(group);
                userRepository.save(user);
                return "Utilisateur ajouté au groupe avec succès.";
            }
            return "Utilisateur déjà membre du groupe.";
        }
        return "Groupe ou utilisateur introuvable.";
    }

    @Override
    public String removeMemberFromGroup(String groupId, String userId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalGroup.isPresent() && optionalUser.isPresent()) {
            Group group = optionalGroup.get();
            User user = optionalUser.get();

            if (group.getMembers().contains(user)) {
                group.removeMember(user);
                user.leaveGroup(group);
                groupRepository.save(group);
                userRepository.save(user);
                return "Utilisateur supprimé du groupe.";
            }
            return "Utilisateur non trouvé dans ce groupe.";
        }
        return "Groupe ou utilisateur introuvable.";
    }


    @Override
    public List<Group> getByMember(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(groupRepository::findByMembersContaining).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /*public List<Group> getByMember(String userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            return user.map(groupRepository::findByMembersContaining)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            System.out.println("Error fetching groups for user {}: {}");
            System.out.println(e.getMessage());
            throw new RuntimeException("An error occurred while retrieving groups", e);
        }
    }*/

    @Override
    public List<Group> getByOwner(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(groupRepository::findByOwner).orElseThrow(() -> new RuntimeException("User not found"));
    }

}


