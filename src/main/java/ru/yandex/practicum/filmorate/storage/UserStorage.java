package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserStorage {
    User create(User user);
    User updateUser(User user);
    Set<User> getUsers();
    void deleteUser(User user);
    User getUserById(int userId);
    }
