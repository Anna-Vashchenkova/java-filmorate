package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserStorage {
    public User create(User user);
    public User updateUser(User user);
    public Set<User> getUsers();
    public void deleteUser(User user);
}
