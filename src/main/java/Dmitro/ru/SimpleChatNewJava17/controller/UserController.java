package Dmitro.ru.SimpleChatNewJava17.controller;

import Dmitro.ru.SimpleChatNewJava17.model.Conversation;
import Dmitro.ru.SimpleChatNewJava17.model.Message;
import Dmitro.ru.SimpleChatNewJava17.model.MidConversation;
import Dmitro.ru.SimpleChatNewJava17.model.User;
import Dmitro.ru.SimpleChatNewJava17.service.UserService;
import Dmitro.ru.SimpleChatNewJava17.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    // выводит список пользователей и обрабатывает пагинацию пользователей
    @GetMapping("/users")
    public String FindAllUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "1") int size,
                               Model model,
                               HttpSession session) {
        List<User> users = new ArrayList<>();
        List<User> findAllUsers = new ArrayList<>();
        Page<User> usersPage = userService.FindAllUsers(page, size);
        if (session.getAttribute("users") == null) {
            for (int i = 0; i < 12; i++) {
                if ((i + page) < userService.FindAllUsers().size())
                    users.add(userService.FindAllUsers().get(i + page));
            }
        }
        else {
            findAllUsers = (List<User>) session.getAttribute("users");
            for (int i = 0; i < 12; i++) {
                if ((i + page) < findAllUsers.size())
                    users.add(findAllUsers.get(i + page));
            }
        }
        // Если пользователи есть, добавляем их в модель
        if (!users.isEmpty()) {
            model.addAttribute("user", userService.getInMemoryUser());
            if (session.getAttribute("users") == null)
                model.addAttribute("allUsers", userService.FindAllUsers());
            else model.addAttribute("allUsers", findAllUsers);
            model.addAttribute("users", users);  // Содержимое текущей страницы
            model.addAttribute("currentPage", page);
            if (session.getAttribute("users") == null)
                model.addAttribute("totalPages", usersPage.getTotalPages());
            else
                model.addAttribute("totalPages", findAllUsers.size());// Общее количество страниц// Получаем список пользователей из страницы// Общее количество страниц
        } else {
            model.addAttribute("user", "No users found");
        }
        return "users"; // Возвращаем имя шаблона
    }

    // обеспечивает окно для авторизации пользователей
    @GetMapping("/entrance")
    public String SimpleReturnShape(Model model) {
        List<User> users = userService.FindAllUsers();
        if (!users.isEmpty()) {
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("users", users);
            model.addAttribute("allow", false);
            model.addAttribute("editUser", new User());

        } else {
            model.addAttribute("user", "No users found");
            model.addAttribute("allow", false);
            model.addAttribute("editUser", new User());
        }
        return "entrance";
    }

    // авторизация пользователей
    @PostMapping("/entranceByEmail")
    public String EntranceByEmail(@RequestParam String email,
                                  @RequestParam String password,
                                  Model model,
                                  HttpSession session) {
        User user = userService.FindUserByEmailAndPassword(email, password);

        // 2. Обработка результата
        if (user != null) {
            userService.setInMemoryUser(user);
            model.addAttribute("user", user);
            session.setAttribute("user", user);
            model.addAttribute("allow", false);
            model.addAttribute("editUser", new User());
            return "entrance"; // страница после успешного входа
        } else {
            model.addAttribute("error", "Неверный email или пароль");
            model.addAttribute("allow", false);
            model.addAttribute("editUser", new User());
            return "entrance"; // вернуться на страницу входа с ошибкой
        }
    }

    // предоставляет окно, который ведёт к методу Registration для регистрации новых пользователей
    @GetMapping("/registrationForm")
    public String RegistrationForm(Model model) {
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("newUser", new User());
        return "registration";
    }

    // обеспечивает добавление новых пользователей
    @PostMapping("/registration")
    public String Registration(@ModelAttribute("newUser") User newUser,
                               Model model,
                               HttpSession session,
                               @RequestParam("testPassword") String testPassword) {
        if(!testPassword.equals(newUser.getPassword())) {
            model.addAttribute("error", "Пароли не сходяться!");
            return "registration";
        }
        else if (userService.FindUserByEmail(newUser.getEmail()) == null) {
            model.addAttribute("newUser", newUser);
            session.setAttribute("user", newUser);
            userService.SaveUser(newUser);
            return "entrance";
        } else {
            model.addAttribute("error", "Такой email уже зарегистрирован!");
            return "registration";
        }
    }

    // Обеспечивает поиск пользователей по email, пагинацию обрабатывая в FindAllUsers по session
    @PostMapping("/findByEmail")
    public String FindUserByEmail(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "12") int size,
                                  @RequestParam("email") String email,
                                  Model model,
                                  HttpSession session) {
        try {
            Page<User> usersPage = userService.FindAllUsers(page, size);
            if(email.isEmpty()){
                session.setAttribute("users", null);
                model.addAttribute("users", usersPage.getContent());
                model.addAttribute("user", userService.getInMemoryUser());
                model.addAttribute("allUsers", userService.FindAllUsers());
                model.addAttribute("currentPage", page);  // Текущая страница
                model.addAttribute("totalPages", usersPage.getTotalPages());  // Общее количество страниц
                model.addAttribute("totalItems", usersPage.getTotalElements());
                return "users";
            }
            List<User> users = userService.FindAllUsers();
            List<User> findOfUsers = new ArrayList<>();
            int count = 0;
            for (User user : users) {
                for (char item : user.getEmail().toCharArray()) {
                    if (item == email.charAt(count)) {
                        if (count < email.length()) {
                            count++;
                            if (count == (email.length())) {
                                findOfUsers.add(user);
                                count = 0;
                            }
                        }
                    }
                    else {
                        count = 0;
                        break;
                    }
                }
            }
            if (findOfUsers.isEmpty()) {
                model.addAttribute("error", "Пользователь с email '" + email + "' не найден.");
                model.addAttribute("users", findOfUsers);
                session.setAttribute("users", findOfUsers);
                model.addAttribute("user", userService.getInMemoryUser());
                model.addAttribute("allUsers", findOfUsers);
                model.addAttribute("currentPage", page);  // Текущая страница
                model.addAttribute("totalPages", usersPage.getTotalPages());  // Общее количество страниц
                model.addAttribute("totalItems", usersPage.getTotalElements());
            }
            else {
                model.addAttribute("users", findOfUsers);
                session.setAttribute("users", findOfUsers);
                model.addAttribute("user", userService.getInMemoryUser());
                model.addAttribute("allUsers", findOfUsers);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", usersPage.getTotalPages());
                model.addAttribute("totalItems", usersPage.getTotalElements());
            }
            return "users";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при поиске пользователя: " + e.getMessage()); // Более информативное сообщение
            model.addAttribute("users", new ArrayList<>());
            return "users";
        }
    }

    // А это маппер для вывода сообщений
    @GetMapping("/users/{id}")
    public String ShapeFormForId(@PathVariable int id, @RequestParam(defaultValue = "false") boolean allow,
                                 Model model, HttpSession session) {
        User userClick = userService.FindUserById(id);
        session.setAttribute("companion", userClick);
        String message = "";

        if (userService.getInMemoryUser() == null || ((User) session.getAttribute("companion")).getId()
                == userService.getInMemoryUser().getId()) {
            Page<User> usersPage = userService.FindAllUsers(0, 10);
            model.addAttribute("users", session.getAttribute("users"));
            session.setAttribute("users", session.getAttribute("users"));
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("allUsers", session.getAttribute("users"));
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", usersPage.getTotalPages());
            model.addAttribute("totalItems", usersPage.getTotalElements());
            return "users";
        }

        List<Message> allMessage = userService.getAllOfMessage();
        Message messagesOfUser = allMessage.stream().
                filter(m -> m.getFirstID() == userService.getInMemoryUser().getId() &&
                        m.getSecondID() == ((User) session.getAttribute("companion")).getId())
                .findFirst().orElse(null);
        if (messagesOfUser == null) {
            messagesOfUser = allMessage.stream().
                    filter(m -> m.getFirstID() == ((User) session.getAttribute("companion")).getId() &&
                            m.getSecondID() == userService.getInMemoryUser().getId())
                    .findFirst().orElse(null);
        }
        if (messagesOfUser == null){
            model.addAttribute("tailOfMessage", new ArrayList<>());
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("userClick", session.getAttribute("companion"));
            model.addAttribute("conversation", null);
            return "PageOfUser";
        }
        List<Message> tailOfMessage = new ArrayList<>();
        Message tempMessages = new Message();
        boolean check = false;
        String temp = "";
        Map<Long, String> userNames = new HashMap<>();
        for (char m : messagesOfUser.getMessage().toCharArray()) {
            if (!check && m == ' ') {
                tempMessages = new Message();
                tempMessages.setSecondID(0);
                tempMessages.setFirstID(Integer.parseInt(temp));
                User user = userService.FindUserById(tempMessages.getFirstID());
                userNames.put(tempMessages.getFirstID(), user.getName() + " " + user.getLastName());
                temp = "";
                check = true;
                continue;
            }
            if (m == '\n' && check) {
                tempMessages.setMessage(temp);
                tailOfMessage.add(tempMessages);
                temp = "";
                check = false;
            }
            else {
                temp += m;
            }
        }
        // переписываю список, убирая сообщения пользователя, удаляя целый блок сообщения и создавая новый
        if (allow) {
            tailOfMessage.clear();
            temp = "";
            for (char m : messagesOfUser.getMessage().toCharArray()) {
                if (!check && m == ' ') {
                    tempMessages = new Message();
                    tempMessages.setSecondID(0);
                    tempMessages.setFirstID(Integer.parseInt(temp));
                    check = true;
                }
                if (m == '\n' && check) {
                    tempMessages.setMessage(temp + '\n');
                    tailOfMessage.add(tempMessages);
                    temp = "";
                    check = false;
                }
                else {
                    temp += m;
                }
            }
            for (int i = tailOfMessage.size() - 1; i >= 0; i--) {
                if (tailOfMessage.get(i).getFirstID() == userService.getInMemoryUser().getId()){
                    tailOfMessage.remove(i);
                    break;
                }
            }
            userService.DeleteMessage(messagesOfUser.getId());
            User companion = (User) session.getAttribute("companion");
            Message newMessage = new Message();
            newMessage.setFirstID(userService.getInMemoryUser().getId());
            newMessage.setSecondID(companion.getId());
            String tempNewMessage = "";
            // Формирую новое сообщение без удалённых блоков сообщений
            for (int i = 0; i < tailOfMessage.size(); i++) {
                tempNewMessage += tailOfMessage.get(i).getMessage();
            }
            if (!tempNewMessage.equals("")) {
                newMessage.setMessage(tempNewMessage);
                userService.AddMessage(newMessage);
            }
            tailOfMessage.clear();
            temp = "";
            allMessage = userService.getAllOfMessage();
            messagesOfUser = allMessage.stream().
                    filter(m -> m.getFirstID() == userService.getInMemoryUser().getId() &&
                            m.getSecondID() == ((User) session.getAttribute("companion")).getId())
                    .findFirst().orElse(null);
            if (messagesOfUser == null) {
                messagesOfUser = allMessage.stream().
                        filter(m -> m.getFirstID() == ((User) session.getAttribute("companion")).getId() &&
                                m.getSecondID() == userService.getInMemoryUser().getId())
                        .findFirst().orElse(null);
            }
            if (messagesOfUser == null){
                model.addAttribute("tailOfMessage", new ArrayList<>());
                model.addAttribute("user", userService.getInMemoryUser());
                model.addAttribute("userClick", session.getAttribute("companion"));
                model.addAttribute("conversation", null);
                return "PageOfUser";
            }
            check = false;
            // формирую обновлённый список
            userNames = new HashMap<>();
            for (char m : messagesOfUser.getMessage().toCharArray()) {
                if (!check && m == ' ') {
                    tempMessages = new Message();
                    tempMessages.setSecondID(0);
                    tempMessages.setFirstID(Integer.parseInt(temp));
                    User user = userService.FindUserById(tempMessages.getFirstID());
                    userNames.put(tempMessages.getFirstID(), user.getName() + " " + user.getLastName());
                    temp = "";
                    check = true;
                    continue;
                }
                if (m == '\n' && check) {
                    tempMessages.setMessage(temp);
                    tailOfMessage.add(tempMessages);
                    temp = "";
                    check = false;
                }
                else {
                    temp += m;
                }
            }
        }
        allow = false;
        model.addAttribute("tailOfMessage", tailOfMessage);
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("userClick", session.getAttribute("companion"));
        model.addAttribute("allow", allow);
        model.addAttribute("conversation", null);
        model.addAttribute("userNames", userNames);
        return "PageOfUser";
    }

    // В этот метод я вынесу часть логики метода ShapeForm для будущей обработки бесед, что довольно опасно
    private void processingOfMessages(HttpSession session) {

    }

    // Я уже начинаю теряться, это маппер для принятия и ввода сообщений
    @PostMapping("/users/message")
    public String InputMessages(@RequestParam("message") String message, Model model, HttpSession session) {
        if (userService.getInMemoryUser() == null || ((User) session.getAttribute("companion")) == null){
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("userClick", session.getAttribute("companion"));
            model.addAttribute("tailOfMessage", null);
            model.addAttribute("conversation", null);
            return "PageOfUser";
        }
        boolean allow = false;
        List<Message> allMessage = userService.getAllOfMessage();
        Message messagesOfUser = allMessage.stream().
                filter(m -> m.getFirstID() == userService.getInMemoryUser().getId() &&
                        m.getSecondID() == ((User) session.getAttribute("companion")).getId())
                .findFirst().orElse(null);
        if (messagesOfUser == null) {
            messagesOfUser = allMessage.stream().
                    filter(m -> m.getFirstID() == ((User) session.getAttribute("companion")).getId() &&
                            m.getSecondID() == userService.getInMemoryUser().getId())
                    .findFirst().orElse(null);
        }
        if (messagesOfUser  == null) {
            Message newMessage = new Message();
            User companion = (User) session.getAttribute("companion");
            newMessage.setFirstID(userService.getInMemoryUser().getId());
            newMessage.setSecondID(companion.getId());
            newMessage.setMessage(userService.getInMemoryUser().getId() + " " + message + '\n');
            userService.AddMessage(newMessage);
            List<Message> tailOfMessage = new ArrayList<>();
            Message tempMessages = new Message();
            boolean check = false;
            String temp = "";
            // этот список я формирую с одним сообщением для цикла в html шаблоне
            for (char m : newMessage.getMessage().toCharArray()) {
                if (!check && m == ' ') {
                    tempMessages = new Message();
                    tempMessages.setSecondID(0);
                    tempMessages.setFirstID(Integer.parseInt(temp));
                    temp = "";
                    check = true;
                    continue;
                }
                if (m == '\n') {
                    tempMessages.setMessage(temp);
                    tailOfMessage.add(tempMessages);
                    temp = "";
                    check = false;
                }
                else {
                    temp += m;
                }
            }
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("userClick", session.getAttribute("companion"));
            model.addAttribute("tailOfMessage", tailOfMessage);
            model.addAttribute("allow", allow);
            model.addAttribute("conversation", null);

            return "PageOfUser";
        }
        else {
            userService.UpdateMessage(messagesOfUser.getId(),
                    userService.getInMemoryUser().getId() + " " + message + '\n');
            // обновляю список сообщений в allMessage
            allMessage = userService.getAllOfMessage();
            messagesOfUser = allMessage.stream().
                    filter(m -> m.getFirstID() == userService.getInMemoryUser().getId() &&
                            m.getSecondID() == ((User) session.getAttribute("companion")).getId())
                    .findFirst().orElse(null);
            if (messagesOfUser == null) {
                messagesOfUser = allMessage.stream().
                        filter(m -> m.getFirstID() == ((User) session.getAttribute("companion")).getId() &&
                                m.getSecondID() == userService.getInMemoryUser().getId())
                        .findFirst().orElse(null);
            }
            List<Message> tailOfMessage = new ArrayList<>();
            Message tempMessages = new Message();
            boolean check = false;
            String temp = "";
            Map<Long, String> userNames = new HashMap<>();
            for (char m : messagesOfUser.getMessage().toCharArray()) {
                if (!check && m == ' ') {
                    tempMessages = new Message();
                    tempMessages.setSecondID(0);
                    tempMessages.setFirstID(Integer.parseInt(temp));
                    User user = userService.FindUserById(tempMessages.getFirstID());
                    userNames.put(tempMessages.getFirstID(), user.getName() + " " + user.getLastName());
                    temp = "";
                    check = true;
                    continue;
                }
                if (m == '\n') {
                    tempMessages.setMessage(temp);
                    tailOfMessage.add(tempMessages);
                    temp = "";
                    check = false;
                }
                else {
                    temp += m;
                }
            }
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("userClick", session.getAttribute("companion"));
            model.addAttribute("conversation", null);
            model.addAttribute("tailOfMessage", tailOfMessage);
            model.addAttribute("allow", allow);
            model.addAttribute("userNames", userNames);
            return "PageOfUser";
        }
    }

    // выводит список с пользователями, с которыми есть переписка, а также может выводить список бесед
    @GetMapping("/historyOfMessages")
    public String historyOfMessages(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "1") int size,
                                    @RequestParam(defaultValue = "false") boolean openConversation,
                                    Model model, HttpSession session) {
        if (userService.getInMemoryUser() == null) {
            return "users";
        }
        if (!openConversation) {
            List<Message> allMessage = userService.getAllOfMessage();
            List<Message> messagesOfUser = allMessage.stream().
                    filter(message -> message.getFirstID() == userService.getInMemoryUser().getId()
                    || message.getSecondID() == userService.getInMemoryUser().getId()).toList();
            List<User> allUsers = userService.FindAllUsers();
            List<User> findOfUsersForDialog = allUsers.stream().
                    filter(user -> messagesOfUser.stream()
                            .anyMatch(message ->
                                    message.getFirstID() == user.getId() || message.getSecondID() == user.getId())).
                    toList();
            // список который захватывает только сообщения с конкрентным пользователем
            List<User> findOfUsersForDialogMore = findOfUsersForDialog.stream().
                    filter(user -> user.getId() != userService.getInMemoryUser().getId()).
                    toList();
            // Добавляю дополнительный список для пагинации
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 12; i++){
                if ((i + page) < findOfUsersForDialogMore.size())
                    users.add(findOfUsersForDialogMore.get(i + page));
            }
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("users", users);
            session.setAttribute("usersOfHistory", users);
            model.addAttribute("userCount", findOfUsersForDialogMore.size());
            session.setAttribute("userCountMore", findOfUsersForDialog.size());
            model.addAttribute("currentPage", page);
            session.setAttribute("currentPage", page);
            model.addAttribute("totalPages", findOfUsersForDialogMore.size());
            session.setAttribute("totalPages", findOfUsersForDialogMore.size());
            model.addAttribute("createOfConversion", false);
            model.addAttribute("openConversation", false);
            return "historyOfMessages";
        }
        else {
            List<MidConversation> midUser = userService.FindAllMidConversations().stream()
                    .filter(midCon -> midCon.getIdOfUser() == userService.getInMemoryUser().getId())
                    .toList();
            List<Conversation> convUser = userService.FindAllConversations().stream()
                    .filter(conv -> midUser.stream()
                            .anyMatch(midCon -> midCon.getIdOfConversation() == conv.getId()))
                    .toList();
            List<Conversation> users = new ArrayList<>();
            for (int i = 0; i < 12; i++){
                if ((i + page) < convUser.size())
                    users.add(convUser.get(i + page));
            }
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("users", users);
            session.setAttribute("usersOfHistory", users);
            model.addAttribute("userCount", convUser.size());
            session.setAttribute("userCountMore", convUser.size());
            model.addAttribute("currentPage", page);
            session.setAttribute("currentPage", page);
            model.addAttribute("totalPages", convUser.size());
            session.setAttribute("totalPages", convUser.size());
            model.addAttribute("createOfConversion", false);
            model.addAttribute("openConversation", true);
            return "historyOfMessages";
        }
    }

    // метод, который может обновлять данные пользователя
    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("editUser") User updatedUser,
                             Model model,
                             HttpSession session,
                             @RequestParam("testPassword") String testPassword) {

        if(!testPassword.equals(updatedUser.getPassword())) {
            model.addAttribute("error", "Пароли не сходяться!");
            return "entrance";
        }
        User existingUser = userService.FindUserById(updatedUser.getId());


        if (existingUser == null) {
            model.addAttribute("error", "Пользователь не найден!");
            return "entrance";
        }

        // Проверка на email: если новый email уже занят другим
        User userWithSameEmail = userService.FindUserByEmail(updatedUser.getEmail());
        if (userWithSameEmail != null && (userWithSameEmail.getId() != updatedUser.getId())) {
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("error", "Email уже используется другим пользователем!");
            model.addAttribute("editUser", updatedUser); // сохранить данные при возврате
            return "entrance";
        }

        userService.UpdateUser(updatedUser);
        session.setAttribute("editUser", updatedUser);
        model.addAttribute("editUser", updatedUser);
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("allow", false);
        model.addAttribute("allowOfDelete", false);// обновляем пользователя в сессии
        return "entrance"; // редирект на профиль
    }

    // метод, который активирует окно с допольнительным подтверждением о удалении аккаунта пользователя
    @GetMapping("deleteUser")
    public String deleteUserByEmail(@RequestParam(defaultValue = "false") boolean allow,
                                    Model model) {
        if (allow) {
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("allow", allow);
            model.addAttribute("allowOfDelete", true);
            model.addAttribute("editUser", new User());
            return "entrance"; // страница после успешного входа
        }
        model.addAttribute("editUser", new User());
        model.addAttribute("user", null);
        model.addAttribute("allow", allow);
        model.addAttribute("allowOfDelete", false);
        return "entrance";
    }

    // само окно с дополнительным поддтверждение о удалении аккаунта пользователя (тут удаляется аккаунт)
    @GetMapping("deleteUser/True")
    public String deleteUserByEmailIfTrue(@RequestParam(defaultValue = "true") boolean allowOfDelete,
                                    Model model) {
        if (allowOfDelete) {
            userService.DeleteUser(userService.getInMemoryUser().getEmail());
            model.addAttribute("user", null);
            model.addAttribute("allow", false);
            model.addAttribute("allowOfDelete", true);
            model.addAttribute("editUser", new User());
            return "entrance"; // страница после успешного входа
        }
        model.addAttribute("user", null);
        model.addAttribute("editUser", new User());
        model.addAttribute("allow", false);
        model.addAttribute("allowOfDelete", false);
        return "entrance";
    }

    // ссылка для выхода из аккаунта пользователя
    @GetMapping("exitFromAccaunt")
    public String exitFromAccaunt(@RequestParam(defaultValue = "false") boolean allow,
                                  Model model, HttpSession session) {
        if (allow) {
            if (userService.getInMemoryUser() != null) {
                userService.setInMemoryUser(null);
                model.addAttribute("user", null);
                model.addAttribute("allow", allow);
                model.addAttribute("allowOfDelete", false);
                model.addAttribute("editUser", new User());
                return "entrance"; // страница после успешного входа
            }
        }
        model.addAttribute("user", null);
        model.addAttribute("editUser", new User());
        model.addAttribute("allow", allow);
        model.addAttribute("allowOfDelete", false);
        return "entrance";
    }

    // ссылка выводит окно для создания беседы
    @GetMapping("/historyOfMessages/createOfConversion")
    public String createOfConversion(Model model, HttpSession session) {
        model.addAttribute("createOfConversion", true);
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("users", session.getAttribute("usersOfHistory"));
        model.addAttribute("userCount", session.getAttribute("userCountMore"));
        model.addAttribute("currentPage", session.getAttribute("currentPage"));
        model.addAttribute("totalPages", session.getAttribute("totalPages"));
        return "historyOfMessages";
    }

    // метод, который получает данные для создания беседы
    @PostMapping("/historyOfMessages/createOfConversionPost")
    public String createOfConversionAction(@RequestParam String nameOfConversion,
                                           @RequestParam boolean AdminIsOwner,
                                           Model model, HttpSession session) {
        Conversation conversation = new Conversation();
        conversation.setNameOfConversation(nameOfConversion);
        conversation.setIDOwner(userService.getInMemoryUser().getId());
        conversation.setAdminIsOwner(AdminIsOwner);
        conversation.setMessage("");
        userService.setNewConversation(conversation);
        conversation = userService.FindAllConversations().stream()
                .filter(conv -> conv.getNameOfConversation().equals(nameOfConversion)
                && conv.getIDOwner() == userService.getInMemoryUser().getId())
                .findFirst().orElse(null);
        MidConversation midConversation = new MidConversation();
        midConversation.setIdOfConversation(conversation.getId());
        midConversation.setIdOfUser(userService.getInMemoryUser().getId());
        userService.setNewMidConversation(midConversation);
        model.addAttribute("createOfConversion", false);
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("users", session.getAttribute("usersOfHistory"));
        model.addAttribute("userCount", session.getAttribute("userCountMore"));
        model.addAttribute("currentPage", session.getAttribute("currentPage"));
        model.addAttribute("totalPages", session.getAttribute("totalPages"));
        return "historyOfMessages";
    }

    @PostMapping("/conversation/{id}/sendMessage")
    public String conversionSendMessage(@RequestParam("message") String message,
                                        @PathVariable("id") long id,
                                        Model model, HttpSession session) {
        System.out.println("Запрос получен! ID: " + id + ", сообщение: " + message);
        Conversation newConversation = new Conversation();
        newConversation.setMessage(userService.getInMemoryUser().getId() + " " + message + '\n');
        Conversation conversation = userService.FindAllConversations().stream()
                .filter(conv -> conv.getId() == id).findFirst().orElse(null);
        if (conversation == null) {
            model.addAttribute("createOfConversion", false);
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("users", session.getAttribute("usersOfHistory"));
            model.addAttribute("userCount", session.getAttribute("userCountMore"));
            model.addAttribute("currentPage", session.getAttribute("currentPage"));
            model.addAttribute("totalPages", session.getAttribute("totalPages"));
            return "historyOfMessages";
        }
        newConversation.setNameOfConversation(conversation.getNameOfConversation());
        userService.UpdateConversation(newConversation);
        conversation = userService.FindAllConversations().stream()
                .filter(conv -> conv.getId() == id).findFirst().orElse(null);
        List<Message> tailOfMessage = new ArrayList<>();
        Message tempMessages = new Message();
        boolean check = false;
        String temp = "";
        Map<Long, String> userNames = new HashMap<>();
        for (char m : conversation.getMessage().toCharArray()) {
            if (!check && m == ' ') {
                tempMessages = new Message();
                tempMessages.setSecondID(0);
                tempMessages.setFirstID(Integer.parseInt(temp));
                User user = userService.FindUserById(tempMessages.getFirstID());
                userNames.put(tempMessages.getFirstID(), user.getName() + " " + user.getLastName());
                temp = "";
                check = true;
                continue;
            }
            if (m == '\n' && check) {
                tempMessages.setMessage(temp);
                tailOfMessage.add(tempMessages);
                temp = "";
                check = false;
            }
            else {
                temp += m;
            }
        }

        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("userClick", null);
        model.addAttribute("conversation", conversation);
        model.addAttribute("tailOfMessage", tailOfMessage);
        model.addAttribute("allow", false);
        model.addAttribute("userNames", userNames);
        return "PageOfUser";
    }

    // сообщения беседы, будут дублированы на примере ShapeFormForId
    @GetMapping("/conversation/{id}")
    public String conversion(@PathVariable long id, @RequestParam(defaultValue = "false") boolean allow,
                             Model model, HttpSession session) {
        Conversation conversation = userService.FindAllConversations().stream()
                .filter(conv -> conv.getId() == id).findFirst().orElse(null);
        if (conversation != null) {
            MidConversation midConversation = userService.FindAllMidConversations().stream()
                    .filter(midConv -> midConv.getIdOfConversation() == id
                            && midConv.getIdOfUser() == userService.getInMemoryUser().getId())
                    .findFirst().orElse(null);
            if (midConversation != null) {
                List<Message> tailOfMessage = new ArrayList<>();
                Message tempMessages = new Message();
                boolean check = false;
                String temp = "";
                Map<Long, String> userNames = new HashMap<>();
                for (char m : conversation.getMessage().toCharArray()) {
                    if (!check && m == ' ') {
                        tempMessages = new Message();
                        tempMessages.setSecondID(0);
                        tempMessages.setFirstID(Integer.parseInt(temp));
                        User user = userService.FindUserById(tempMessages.getFirstID());
                        userNames.put(tempMessages.getFirstID(), user.getName() + " " + user.getLastName());
                        temp = "";
                        check = true;
                        continue;
                    }
                    if (m == '\n' && check) {
                        tempMessages.setMessage(temp);
                        tailOfMessage.add(tempMessages);
                        temp = "";
                        check = false;
                    }
                    else {
                        temp += m;
                    }
                }

                model.addAttribute("user", userService.getInMemoryUser());
                model.addAttribute("userClick", null);
                model.addAttribute("conversation", conversation);
                model.addAttribute("tailOfMessage", tailOfMessage);
                model.addAttribute("allow", false);
                model.addAttribute("userNames", userNames);
                return "PageOfUser";
            }
        }
        model.addAttribute("createOfConversion", false);
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("users", session.getAttribute("usersOfHistory"));
        model.addAttribute("userCount", session.getAttribute("userCountMore"));
        model.addAttribute("currentPage", session.getAttribute("currentPage"));
        model.addAttribute("totalPages", session.getAttribute("totalPages"));
        return "historyOfMessages";
    }
    // переписываю список, убирая сообщения пользователя, удаляя целый блок сообщения и создавая новый
                /*
                if (allow) {
                    tailOfMessage.clear();
                    temp = "";
                    for (char m : conversation.getMessage().toCharArray()) {
                        if (!check && m == ' ') {
                            tempMessages = new Message();
                            tempMessages.setSecondID(0);
                            tempMessages.setFirstID(Integer.parseInt(temp));
                            check = true;
                        }
                        if (m == '\n' && check) {
                            tempMessages.setMessage(temp + '\n');
                            tailOfMessage.add(tempMessages);
                            temp = "";
                            check = false;
                        }
                        else {
                            temp += m;
                        }
                    }
                    for (int i = tailOfMessage.size() - 1; i >= 0; i--) {
                        if (tailOfMessage.get(i).getFirstID() == userService.getInMemoryUser().getId()){
                            tailOfMessage.remove(i);
                            break;
                        }
                    }
                    userService.deleteConversationById(conversation.getId());
                    Message newMessage = new Message();
                    newMessage.setFirstID(userService.getInMemoryUser().getId());
                    String tempNewMessage = "";
                    // Формирую новое сообщение без удалённых блоков сообщений
                    for (int i = 0; i < tailOfMessage.size(); i++) {
                        tempNewMessage += tailOfMessage.get(i).getMessage();
                    }
                    if (!tempNewMessage.equals("")) {
                        newMessage.setMessage(tempNewMessage);
                        userService.AddMessage(newMessage);
                    }
                    tailOfMessage.clear();
                    temp = "";
                 */
}













