package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Set<User> getUsers() {
        return userStorage.getUsers();
    }

    public void deleteUser(int userId) {
        User user = userStorage.getUserById(userId);
        userStorage.deleteUser(user);
    }

    public Set<Integer> addFriends(int userId, int friendsId) {
        User user = userStorage.getUserById(userId);
        Set<Integer> userFriends = user.getFriends();
        if (userFriends.contains(friendsId)) {
            return userFriends;
        }
        userFriends.add(friendsId);
        return userFriends;
    }

    public void deleteFriends(int userId, int friendsId) {
        User user = userStorage.getUserById(userId);
        Set<Integer> userFriends = user.getFriends();
        if (userFriends.contains(friendsId)) {
            userFriends.remove(friendsId);
        } else {
            throw new ValidationException("В списке друзей нет пользователя с таким id");
        }
    }

    public Set<Integer> commonFriends(int userId1, int userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        Set<Integer> first = user1.getFriends();
        Set<Integer> second = user2.getFriends();
        Set<Integer> result = first.stream().filter(element -> second.contains(element)).collect(Collectors.toSet());
        return result;
    }

    public User getById(int userId) {
        return userStorage.getUserById(userId);
    }
}
