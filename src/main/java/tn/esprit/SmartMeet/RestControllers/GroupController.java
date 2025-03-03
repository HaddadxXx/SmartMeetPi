package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.SmartMeet.Services.Group.IGroupService;
import tn.esprit.SmartMeet.DAO.Entities.Group;


import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "http://localhost:4200")

public class GroupController {

    @Autowired
    private IGroupService groupService;

    @PostMapping("/create")
    public Group createGroup(@RequestPart("group") Group group,
                             @RequestPart(value = "file", required = false) MultipartFile file) {
        return groupService.createGroup(group, file);
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