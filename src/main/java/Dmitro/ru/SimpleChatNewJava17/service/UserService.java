package Dmitro.ru.SimpleChatNewJava17.service;

import Dmitro.ru.SimpleChatNewJava17.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> FindAllUsers();
    User FindUserById(long id);
    User FindUserByEmail(String email);
    Page<User> FindAllUsers(int page, int size);
    User SaveUser(User user);
    User FindUserByEmailAndPassword(String email, String password);
    User UpdateUser(User user);
    void DeleteUser(String email);
    User getInMemoryUser();
    void setInMemoryUser(User user);
}
