package splitwise.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Group {
    private final String id;
    private String name;
    private final Set<User> members = new HashSet<>();

    public Group(String id, String name) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public void addMember(User user) {
        members.add(Objects.requireNonNull(user, "user must not be null"));
    }

    public boolean hasMember(User user) {
        return members.contains(user);
    }

    public Set<User> getMembers() {
        return Collections.unmodifiableSet(members);
    }
}


