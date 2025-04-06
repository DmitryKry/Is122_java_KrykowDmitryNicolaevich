package Dmitro.ru.SimpleChatNewJava17.service.impl;

import Dmitro.ru.SimpleChatNewJava17.model.User;
import Dmitro.ru.SimpleChatNewJava17.repository.InMemoryUserDAO;
import Dmitro.ru.SimpleChatNewJava17.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemoryUserServiceImpl implements UserService {
    private final InMemoryUserDAO userDAO;
    @Override
    public List<User> FindAllUsers() {
        return userDAO.FindAllUsers();
    }

    @Override
    public User FindUserById(long id) {
        return userDAO.FindUserById(id);
    }

    @Override
    public User FindUserByEmail(String email) {
        return userDAO.FindUserByEmail(email);
    }

    @Override
    public User SaveUser(User user) {
        return userDAO.SaveUser(user);
    }

    @Override
    public User FindUserByEmailAndPassword(String email, String password) {
        return userDAO.FindUserByEmail(email);
    }

    @Override
    public User UpdateUser(User user) {
        return userDAO.UpdateUser(user);
    }

    @Override
    public void DeleteUser(String email) {
        userDAO.DeleteUser(email);
    }

    @Override
    public User getInMemoryUser() {
        return null;
    }

    @Override
    public void setInMemoryUser(User user) {
    }
}
