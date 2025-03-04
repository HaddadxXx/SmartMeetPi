package tn.esprit.SmartMeet.DAO.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.SmartMeet.DAO.Entities.Group;
import tn.esprit.SmartMeet.DAO.Entities.User;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    List<Group> findByOwner(User owner);
    List<Group> findByMembersContaining(User member);
}