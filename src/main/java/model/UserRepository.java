package model;

import java.util.*;

public class UserRepository {
    private static final Map<Long, User> users = new HashMap<>();
    private static long sequence = 0L;

    public User save(User user) {
        user.setId(++sequence);
        users.put(user.getId(), user);
        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public User findByUserId(String userId) {
        for (User user : users.values()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
