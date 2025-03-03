package tn.esprit.SmartMeet.Services.Group;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.DAO.Entities.Group;

import java.util.List;

public interface IGroupService {
    public String uploadPhoto(MultipartFile file);
    public Group createGroup(Group group, MultipartFile file);  // Créer un groupe
    Group getGroupById(String groupId);  // Récupérer un groupe par ID
    List<Group> getAllGroups();  // Récupérer tous les groupes
    Group updateGroup(String groupId, Group updatedGroup);  // Mettre à jour un groupe
    void deleteGroup(String groupId);  // Supprimer un groupe

    String addMemberToGroup(String groupId, String userId);  // Ajouter un membre
    String removeMemberFromGroup(String groupId, String userId);  // Supprimer un membre
}

