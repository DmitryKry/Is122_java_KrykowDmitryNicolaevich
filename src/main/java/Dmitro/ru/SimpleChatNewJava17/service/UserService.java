package Dmitro.ru.SimpleChatNewJava17.service;

import Dmitro.ru.SimpleChatNewJava17.model.User;

import java.util.List;

public interface UserService {
    List<User> FindAllUsers();
    User FindUserById(long id);
    User FindUserByEmail(String email);
    User SaveUser(User user);
    User FindUserByEmailAndPassword(String email, String password);
    User UpdateUser(User user);
    void DeleteUser(String email);
}
