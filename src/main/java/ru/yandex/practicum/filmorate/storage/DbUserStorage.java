package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Optional;
import java.util.Set;

@Component("databaseUS")
public class DbUserStorage implements UserStorage {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Set<User> getUsers() {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public Optional<User> getUserById(int userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return Optional.empty();
    }
}
