package Dmitro.ru.SimpleChatNewJava17.controller;

import Dmitro.ru.SimpleChatNewJava17.model.User;
import Dmitro.ru.SimpleChatNewJava17.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/users")
    public List<User> FindAllUsers() {
        return userService.FindAllUsers();
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
        return userService.FindUserByEmail(email) == null ? null : userService.FindUserByEmail(email);
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