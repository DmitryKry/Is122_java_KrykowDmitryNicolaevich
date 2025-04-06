package Dmitro.ru.SimpleChatNewJava17.repository;

import Dmitro.ru.SimpleChatNewJava17.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemoryUserDAO {
    private final List<User> users = new ArrayList<>();

    public List<User> FindAllUsers() {
        return users;
    }
    public User FindUserById(long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    public User FindUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    public User SaveUser(User user) {
        users.add(user);
        return user;
    }

    public User UpdateUser(User user) {
        var userIndex = IntStream.range(0, users.size())
                .filter(i -> users.get(i).getEmail().equals(user.getEmail()))
                .findFirst().orElse(-1);
        if (userIndex > -1) {
            users.set(userIndex, user);
            return user;
        }
        return null;
    }

    public void DeleteUser(String email) {
        var user = FindUserByEmail(email);
        if (user != null) {
            users.remove(user);
        }
    }
}
