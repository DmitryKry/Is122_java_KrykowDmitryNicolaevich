package Dmitro.ru.SimpleChatNewJava17.controller;

import Dmitro.ru.SimpleChatNewJava17.model.Message;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
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
            for (int i = 0; i < 10; i++) {
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

    @GetMapping("/entrance")
    public String SimpleReturnShape(Model model) {
        List<User> users = userService.FindAllUsers();
        if (!users.isEmpty()) {
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("users", users);
        } else {
            model.addAttribute("user", "No users found");
        }
        return "entrance";
    }


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
            return "entrance"; // страница после успешного входа
        } else {
            model.addAttribute("error", "Неверный email или пароль");
            return "entrance"; // вернуться на страницу входа с ошибкой
        }
    }

    @GetMapping("/registrationForm")
    public String RegistrationForm(Model model) {
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("newUser", new User());
        return "registration";
    }


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
    public String ShapeFormForId(@PathVariable int id, Model model, HttpSession session) {
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
            return "PageOfUser";
        }
        List<Message> tailOfMessage = new ArrayList<>();
        Message tempMessages = new Message();
        tempMessages.setSecondID(0);
        boolean check = false;
        String temp = "";
        for (char m : messagesOfUser.getMessage().toCharArray()) {
            if (!check && m == ' ') {
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
        tempMessages.setMessage(temp);
        tailOfMessage.add(tempMessages);
        model.addAttribute("tailOfMessage", tailOfMessage);
        model.addAttribute("user", userService.getInMemoryUser());
        model.addAttribute("userClick", session.getAttribute("companion"));
        return "PageOfUser";
    }

    // Я уже начинаю теряться, это маппер для принятия и ввода сообщений
    @PostMapping("/users/message")
    public String InputMessages(@RequestParam("message") String message, Model model, HttpSession session) {
        if (userService.getInMemoryUser() == null || ((User) session.getAttribute("companion")) == null){
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("userClick", session.getAttribute("companion"));
            model.addAttribute("tailOfMessage", null);
            return "PageOfUser";
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
        if (messagesOfUser  == null) {
            Message newMessage = new Message();
            User companion = (User) session.getAttribute("companion");
            newMessage.setFirstID(userService.getInMemoryUser().getId());
            newMessage.setSecondID(companion.getId());
            newMessage.setMessage(userService.getInMemoryUser().getId() + " " +
                    userService.getInMemoryUser().getName() + " - " + message);

            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("userClick", session.getAttribute("companion"));
            model.addAttribute("tailOfMessage", userService.getInMemoryUser().getId()
                    + " " + message);
            userService.AddMessage(newMessage);
            return "PageOfUser";
        }
        else {
            userService.UpdateMessage(messagesOfUser.getId(),
                    userService.getInMemoryUser().getId() + " " + message);
            /*tailOfMessage.add("\n" +
                    userService.getInMemoryUser().getId() + message);*/
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
            tempMessages.setSecondID(0);
            boolean check = false;
            String temp = "";
            for (char m : messagesOfUser.getMessage().toCharArray()) {
                if (!check && m == ' ') {
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
            tempMessages.setMessage(temp);
            tailOfMessage.add(tempMessages);
            model.addAttribute("user", userService.getInMemoryUser());
            model.addAttribute("userClick", session.getAttribute("companion"));
            model.addAttribute("tailOfMessage", tailOfMessage);
            return "PageOfUser";
        }
    }

    @GetMapping("/historyOfMessages")
    public String historyOfMessages(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "1") int size,
                                    Model model, HttpSession session) {
        if (userService.getInMemoryUser() == null) {
            return "users";
        }
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
        model.addAttribute("userCount", findOfUsersForDialogMore.size());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", findOfUsersForDialogMore.size());
        return "historyOfMessages";
    }

    @GetMapping("/{email}")
    public User FindUserByEmail(@PathVariable String email) {
        return userService.FindUserByEmail(email);
    }

    @PutMapping("updateUser")
    public User updateUser(@RequestBody User user) {
        return userService.UpdateUser(user);
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<User> FindUserById(@PathVariable long id) {
        User user = userService.FindUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("deleteUser/{email}")
    public void deleteUserByEmail(@PathVariable String email) {
        userService.DeleteUser(email);
    }
}