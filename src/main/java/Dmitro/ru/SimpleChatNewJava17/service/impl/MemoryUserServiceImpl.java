package Dmitro.ru.SimpleChatNewJava17.service.impl;

import Dmitro.ru.SimpleChatNewJava17.model.Conversation;
import Dmitro.ru.SimpleChatNewJava17.model.Message;
import Dmitro.ru.SimpleChatNewJava17.model.MidConversation;
import Dmitro.ru.SimpleChatNewJava17.model.User;
import Dmitro.ru.SimpleChatNewJava17.repository.InMemoryUserDAO;
import Dmitro.ru.SimpleChatNewJava17.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemoryUserServiceImpl implements UserService {
    private final InMemoryUserDAO userDAO;

    @Override
    public List<User> getListUsersForAddConversation() {
        return List.of();
    }

    @Override
    public List<User> FindAllUsers() {
        return userDAO.FindAllUsers();
    }

    @Override
    public Page<User> FindAllUsers(int page, int size){
        return null;
    }

    @Override
    public Message FindMessageById(long id) {
        return null;
    }

    @Override
    public List<Message> getAllOfMessage() {
        return List.of();
    }

    @Override
    public Message AddMessage(Message message) {
        return null;
    }

    @Override
    public Message UpdateMessage(long id, String newMessageContent) {
        return null;
    }

    @Override
    public void DeleteMessage(long id) {

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

    @Override
    public Conversation FindConversationById(String name) {
        return null;
    }

    @Override
    public Conversation setNewConversation(Conversation newConversation) {
        return null;
    }

    @Override
    public List<Conversation> FindAllConversations() {
        return List.of();
    }

    @Override
    public void deleteConversationById(long id) {

    }

    @Override
    public Conversation UpdateConversation(Conversation newConversation) {
        return null;
    }

    @Override
    public MidConversation FindMidConversationById(String nameOfConversation, long idOfUser) {
        return null;
    }

    @Override
    public MidConversation setNewMidConversation(MidConversation newConversation) {
        return null;
    }

    @Override
    public List<MidConversation> FindAllMidConversations() {
        return List.of();
    }

    @Override
    public void deleteMidConversationById(long id) {

    }
}
