package Dmitro.ru.SimpleChatNewJava17.controller;

import Dmitro.ru.SimpleChatNewJava17.model.User;
import Dmitro.ru.SimpleChatNewJava17.service.UserService;
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
<<<<<<< HEAD
                               @RequestParam(defaultValue = "1") int size,
                               Model model) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            if ((i + page) < i + userService.FindAllUsers().size()) users.add(userService.FindAllUsers().get(i + page));
        }
=======
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        List<User> users = userService.FindAllUsers();
        Page<User> usersPage = userService.FindAllUsers(page, size);
>>>>>>> 853c40d0dacb3b913cde403bcdf76fc1da8b6cac

        // Если пользователи есть, добавляем их в модель
        if (!users.isEmpty()) {
            model.addAttribute("user", userService.getInMemoryUser());
<<<<<<< HEAD
            model.addAttribute("users", users);
            model.addAttribute("allUsers", userService.FindAllUsers());
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);// Получаем список пользователей из страницы// Общее количество страниц
=======
            model.addAttribute("allUsers", userService.FindAllUsers());
            model.addAttribute("users", usersPage.getContent());  // Содержимое текущей страницы
            model.addAttribute("currentPage", page);  // Текущая страница
            model.addAttribute("totalPages", usersPage.getTotalPages());  // Общее количество страниц
            model.addAttribute("totalItems", usersPage.getTotalElements());  // Получаем список пользователей из страницы// Общее количество страниц
>>>>>>> 853c40d0dacb3b913cde403bcdf76fc1da8b6cac
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
    public String FindUserByEmail(@RequestParam("email") String email, Model model) {
        try {
            if(email.isEmpty()){
                model.addAttribute("users", userService.FindAllUsers());
                model.addAttribute("user", userService.getInMemoryUser());
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
            }
            else {
                model.addAttribute("users", findOfUsers);
                model.addAttribute("user", userService.getInMemoryUser());
            }
            return "users";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при поиске пользователя: " + e.getMessage()); // Более информативное сообщение
            model.addAttribute("users", new ArrayList<>());
            return "users";
        }
    }

    @PostMapping("saveUser")
    public ResponseEntity<String> SaveUser(@RequestBody User user) {
        try {
            userService.SaveUser(user);
            return ResponseEntity.ok("Success save user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving user: " + e.getMessage());
        }
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