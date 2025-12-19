package splitwise.service;

import splitwise.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public User createUser(String id, String name) {
        if (users.containsKey(id)) {
            throw new IllegalArgumentException("User with ID " + id + " already exists");
        }
        User user = new User(id, name);
        users.put(id, user);
        return user;
    }

    public Optional<User> getUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public User getUserByIdOrThrow(String id) {
        return getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " not found"));
    }

    public Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }
}

