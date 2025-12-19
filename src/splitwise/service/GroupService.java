package splitwise.service;

import splitwise.model.Group;
import splitwise.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GroupService {
    private final Map<String, Group> groups = new HashMap<>();
    private final UserService userService;

    public GroupService(UserService userService) {
        this.userService = userService;
    }

    public Group createGroup(String id, String name) {
        if (groups.containsKey(id)) {
            throw new IllegalArgumentException("Group with ID " + id + " already exists");
        }
        Group group = new Group(id, name);
        groups.put(id, group);
        return group;
    }

    public void addUserToGroup(String groupId, String userId) {
        Group group = getGroupByIdOrThrow(groupId);
        User user = userService.getUserByIdOrThrow(userId);
        group.addMember(user);
    }

    public Optional<Group> getGroupById(String id) {
        return Optional.ofNullable(groups.get(id));
    }

    public Group getGroupByIdOrThrow(String id) {
        return getGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group with ID " + id + " not found"));
    }

    public Map<String, Group> getAllGroups() {
        return new HashMap<>(groups);
    }

    public UserService getUserService() {
        return userService;
    }
}

