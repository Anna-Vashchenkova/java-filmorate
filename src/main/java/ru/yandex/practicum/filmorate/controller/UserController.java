package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId")  int userId) {
        userService.deleteUser(userId);
    }

    @PutMapping(value = "/{userId}/friends/{friendsId}")
    public Set<Integer> addFriends(@PathVariable int userId, @PathVariable int friendsId) {
        return  userService.addFriends(userId, friendsId);
    }

    @DeleteMapping("/{userId}/friends/{friendsId}")
    public void deleteFriends(@PathVariable int userId, @PathVariable int friendsId) {
        userService.deleteFriends(userId, friendsId);
    }

    @GetMapping("/{userId}/friends")
    public Set<User> getFriends(@PathVariable int userId) {
        Set<User> friends = userService.getFriends(userId);
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{userId2}")
    public Set<User> commonFriends(@PathVariable int userId, @PathVariable int userId2) {
        Set<User> users = userService.commonFriends(userId, userId2);
        return users;
    }
}