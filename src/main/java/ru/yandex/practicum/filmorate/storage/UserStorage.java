package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User updateUser(User user);

    Collection<User> getUsers();

    void deleteUser(User user);

    Optional<User> getUserById(int userId);

    Optional<User> getByEmail(String email);
}
