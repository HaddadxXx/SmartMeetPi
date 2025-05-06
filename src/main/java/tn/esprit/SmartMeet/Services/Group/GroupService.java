package tn.esprit.SmartMeet.Services.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
import tn.esprit.SmartMeet.exeptions.NotScienceDomainException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import java.util.List;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    private final String UPLOAD_DIRECTORY = "uploads/";

    @Value("${gemini.api.key}")
    private String apiKey ;

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
        // Authentication and user retrieval logic (unchanged)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        String email;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // getUsername() retourne l'email
        } else {
            throw new RuntimeException("Impossible de récupérer l'email de l'utilisateur");
        }

        // Retrieve user from database
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));


        // Check if the group is related to science
        if (!isRelatedToScience(group.getName(), group.getDescription())) {
            System.out.println(group.getName()+"//"+group.getDescription());
            System.out.println("Le groupe n'est pas lié au domaine scientifique.");
            throw new NotScienceDomainException("Le groupe n'est pas lié au domaine scientifique.");
        }

        // Set owner, add to members, handle photo
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

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-002:generateContent?key=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt))
                ))
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                System.out.println("Response Body: " + body);
                if (body != null) {
                    String answer = extractAnswer(body);  // Pass the full response body here
                    System.out.println("Extracted answer: " + answer);
                    return answer != null && answer.equalsIgnoreCase("yes");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la vérification avec Gemini: " + e.getMessage());
        }
        return false;
    }

    private String extractAnswer(Map<String, Object> responseBody) {
        if (responseBody == null) {
            return null;
        }

        // Get the candidates array from the response
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");

        if (candidates == null || candidates.isEmpty()) {
            System.out.println("No candidates found in response");
            return null;
        }

        // Get the first candidate
        Map<String, Object> firstCandidate = candidates.get(0);
        if (firstCandidate == null) {
            System.out.println("First candidate is null");
            return null;
        }

        // Get the content from the candidate
        Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
        if (content == null) {
            System.out.println("Content is null in candidate");
            return null;
        }

        // Get the parts from the content
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        if (parts == null || parts.isEmpty()) {
            System.out.println("No parts found in content");
            return null;
        }

        // Get the first part
        Map<String, Object> firstPart = parts.get(0);
        if (firstPart == null || !firstPart.containsKey("text")) {
            System.out.println("No text in first part");
            return null;
        }

        // Return the trimmed text
        return firstPart.get("text").toString().trim();
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



    @Override
    public List<Group> getByOwner(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(groupRepository::findByOwner).orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Renvoie la liste de tous les groupes,
     * sauf ceux où l'utilisateur (userId) est membre.
     */
    @Override
    public List<Group> getAllExceptMember(String userId) {
        // 1. récupère la liste complète
        List<Group> allGroups = groupRepository.findAll();

        // 2. récupère les groupes dont il est membre
        List<Group> memberGroups = getByMember(userId);

        // 3. filtre : tous sauf ceux-ci
        return allGroups.stream()
                .filter(g -> !memberGroups.contains(g))
                .toList();
    }

}


