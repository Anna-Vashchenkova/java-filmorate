package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<User> getUsers() {
        List<User> users1 = new ArrayList<>();
        users1.addAll(users);
        return users1;
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

    @Override
    public List<User> findUsersByIds(Set<Integer> ids) {
        return users.stream()
                .filter(user -> ids.contains(user.getId()))
                .collect(Collectors.toList());
    }
}
