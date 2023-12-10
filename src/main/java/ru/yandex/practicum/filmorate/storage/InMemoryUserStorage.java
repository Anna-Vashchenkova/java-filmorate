package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component("inMemoryUS")
public class InMemoryUserStorage implements UserStorage {
    protected final Set<User> users = new HashSet<>();
    protected int lastId = 0;

    @Override
    public User create(User user) {
        user.setId(++lastId);
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.add(user);
        return user;
    }

    @Override
    public Set<User> getUsers() {
        return users;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail() == email)
                .findFirst();
    }
}
