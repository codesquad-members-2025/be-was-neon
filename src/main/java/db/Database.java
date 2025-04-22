package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        logger.debug("User added: {}", user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
