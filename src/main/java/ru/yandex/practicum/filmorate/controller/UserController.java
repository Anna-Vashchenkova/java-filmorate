package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final InMemoryUserStorage userStorage;

    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping
    public Set<User> getUsers() {
        return userStorage.getUsers();
    }

    @DeleteMapping
    public void delete(User user) {
        userStorage.deleteUser(user);
    }
}