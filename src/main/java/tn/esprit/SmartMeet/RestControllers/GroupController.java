package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.SmartMeet.Services.Group.IGroupService;
import tn.esprit.SmartMeet.DAO.Entities.Group;


import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private IGroupService groupService;

    /*@PostMapping
    public Group createGroup(@RequestBody Group group) {
        System.out.println("🔹 JSON reçu: " + group.getName());
        return groupService.createGroup(group);
    }*/
    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        System.out.println("🚀 Données reçues : " + group);
        groupService.createGroup(group);
        return ResponseEntity.ok(group);
    }


    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable String id) {
        return groupService.getGroupById(id);
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable String id, @RequestBody Group group) {
        return groupService.updateGroup(id, group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable String id) {
        groupService.deleteGroup(id);
    }

    @PostMapping("/{groupId}/addMember/{userId}")
    public String addMemberToGroup(@PathVariable String groupId, @PathVariable String userId) {
        return groupService.addMemberToGroup(groupId, userId);
    }

    @DeleteMapping("/{groupId}/removeMember/{userId}")
    public String removeMemberFromGroup(@PathVariable String groupId, @PathVariable String userId) {
        return groupService.removeMemberFromGroup(groupId, userId);
    }
}