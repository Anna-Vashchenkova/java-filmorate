package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    Set<User> users = new HashSet<>();
    private int lastId = 0;

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if ((user.getEmail() == null) || (user.getEmail().isEmpty()) || (!user.getEmail().contains("@"))) {
            throw new ValidationException("в переданных данных электронная почта не может быть пустой и должна содержать символ @");
        }
        if ((user.getLogin().isEmpty()) || (user.getLogin().contains(" "))) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (users.contains(user)) {
            throw new ValidationException("пользователь с указанным адресом электронной почты уже был добавлен ранее");
        }
        user.setId(++lastId);
        users.add(user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (user.getEmail().isEmpty()) {
            throw new ValidationException("в переданных данных отсутствует адрес электронной почты");
        } else {
            Optional<User> optionalUser = getUsers().stream().filter(user1 -> user1.getId() == user.getId()).findFirst();
            if (optionalUser.isEmpty()) {
                throw new ValidationException("пользователя с таким Id не существует");
            }
            User userUpdate = optionalUser.get();
            userUpdate.setEmail(user.getEmail());
            userUpdate.setLogin(user.getLogin());
            userUpdate.setName(user.getName());
            userUpdate.setBirthday(user.getBirthday());
            return user;
        }
    }

    @GetMapping
    public Set<User> getUsers() {
        return users;
    }
}