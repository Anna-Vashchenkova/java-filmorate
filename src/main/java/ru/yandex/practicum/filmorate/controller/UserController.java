package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
    public Set<User> getUsers() {
        return userService.getUsers();
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
    public Set<Integer> getFriends(@PathVariable int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{userId2}")
    public Set<Integer> commonFriends(@PathVariable int userId, @PathVariable int userId2) {
        return userService.commonFriends(userId, userId2);
    }
}