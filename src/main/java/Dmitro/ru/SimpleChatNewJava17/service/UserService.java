package Dmitro.ru.SimpleChatNewJava17.service;

import Dmitro.ru.SimpleChatNewJava17.model.Conversation;
import Dmitro.ru.SimpleChatNewJava17.model.Message;
import Dmitro.ru.SimpleChatNewJava17.model.MidConversation;
import Dmitro.ru.SimpleChatNewJava17.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getListUsersForAddConversation();
    List<User> FindAllUsers();
    User FindUserById(long id);
    User FindUserByEmail(String email);
    Page<User> FindAllUsers(int page, int size);
    Message FindMessageById(long id);
    List<Message> getAllOfMessage();
    Message AddMessage(Message message);
    Message UpdateMessage(long id, String newMessageContent);
    void DeleteMessage(long id);
    User SaveUser(User user);
    User FindUserByEmailAndPassword(String email, String password);
    User UpdateUser(User user);
    void DeleteUser(String email);
    User getInMemoryUser();
    void setInMemoryUser(User user);
    Conversation FindConversationById(String name);
    Conversation setNewConversation(Conversation newConversation);
    List<Conversation> FindAllConversations();
    void deleteConversationById(long id);
    Conversation UpdateConversation(Conversation newConversation);
    MidConversation FindMidConversationById(String nameOfConversation, long idOfUser);
    MidConversation setNewMidConversation(MidConversation newConversation);
    List<MidConversation> FindAllMidConversations();
    void deleteMidConversationById(long id);
}
