package tn.esprit.SmartMeet.Services.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.SmartMeet.DAO.Repositories.GroupRepository;
import tn.esprit.SmartMeet.DAO.Repositories.UserRepository;
import tn.esprit.SmartMeet.DAO.Entities.Group;
import tn.esprit.SmartMeet.DAO.Entities.User;

import java.util.Optional;

import java.util.List;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

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
}


