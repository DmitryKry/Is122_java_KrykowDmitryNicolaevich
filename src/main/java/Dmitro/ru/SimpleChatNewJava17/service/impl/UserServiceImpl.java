package Dmitro.ru.SimpleChatNewJava17.service.impl;

import Dmitro.ru.SimpleChatNewJava17.model.Message;
import Dmitro.ru.SimpleChatNewJava17.model.User;
import Dmitro.ru.SimpleChatNewJava17.repository.InMemoryUserDAO;
import Dmitro.ru.SimpleChatNewJava17.repository.MessageRepository;
import Dmitro.ru.SimpleChatNewJava17.repository.UserRepository;
import Dmitro.ru.SimpleChatNewJava17.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InMemoryUserDAO userDAO;
    private final MessageRepository messageRepository;
    @Override
    public List<User> FindAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> FindAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    @Override
    public Message FindMessageById(long id) {
        return messageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Message> getAllOfMessage() {
        return messageRepository.findAll();
    }

    @Override
    public Message AddMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message UpdateMessage(long id, String newMessageContent) {
        Message existingMessage = messageRepository.findById(id).orElse(null);
        if (existingMessage != null) {
            existingMessage.setMessage(existingMessage.getMessage() + newMessageContent);
            return messageRepository.save(existingMessage);
        }
        return null;
    }

    @Override
    public void DeleteMessage(long id) {
        Message messageForDrop = messageRepository.findAll().stream().
                filter(message -> message.getId() == id).findFirst().orElse(null);
        if (messageForDrop != null) {
            messageRepository.delete(messageForDrop);
        }
    }

    @Override
    public User FindUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User FindUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User FindUserByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User SaveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User with this email already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User UpdateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            // Обновляем поля существующего пользователя
            existingUser.setName(user.getName());
            existingUser.setLastName(user.getLastName());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setGender(user.getGender());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            return userRepository.save(existingUser);
        }
        return null;
    }

    @Override
    public void DeleteUser(String email) {
        User user = FindUserByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public User getInMemoryUser() {
        return userDAO.GetInMemoryUserDAO();
    }

    @Override
    public void setInMemoryUser(User user) {
        userDAO.SetInMemoryUserDAO(user);
    }
}
