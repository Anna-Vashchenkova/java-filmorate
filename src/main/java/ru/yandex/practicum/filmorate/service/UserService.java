package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
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
        } else if (userStorage.getByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("пользователь с указанным адресом электронной почты уже был добавлен ранее");
        }
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (user == null) {
            throw new DataNotFoundException("Пользователь не найден.");
        }
        if (user.getEmail().isEmpty()) {
            throw new ValidationException("в переданных данных отсутствует адрес электронной почты");
        }
        Optional<User> optionalUser = userStorage.getUserById(user.getId());
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Пользователь не найден.");
        }
        User userUpdate = optionalUser.get();
        userUpdate.setEmail(user.getEmail());
        userUpdate.setLogin(user.getLogin());
        userUpdate.setName(user.getName());
        userUpdate.setBirthday(user.getBirthday());
        userUpdate.setFriends(user.getFriends());
        return userStorage.updateUser(user);
    }

    public Set<User> getUsers() {
        return userStorage.getUsers();
    }

    public void deleteUser(int userId) {
        Optional<User> optionalUser = userStorage.getUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Пользователь не найден.");
        }
        userStorage.deleteUser(optionalUser.get());
    }

    public Set<Integer> addFriends(int userId, int friendsId) {
        User user = userStorage.getUserById(userId).get();
        Set<Integer> userFriends = user.getFriends();
        if (userFriends.contains(friendsId)) {
            return userFriends;
        }
        userFriends.add(friendsId);
        return userFriends;
    }

    public void deleteFriends(int userId, int friendsId) {
        User user = userStorage.getUserById(userId).get();
        Set<Integer> userFriends = user.getFriends();
        if (userFriends.contains(friendsId)) {
            userFriends.remove(friendsId);
        } else {
            throw new ValidationException("В списке друзей нет пользователя с таким id");
        }
    }

    public Set<Integer> getFriends(int userId) {
        Optional<User> userOptional = userStorage.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        User result = userOptional.get();
        return result.getFriends();
    }
    public Set<Integer> commonFriends(int userId1, int userId2) {
        Optional<User> userOptional = userStorage.getUserById(userId1);
        Optional<User> userOptional2 = userStorage.getUserById(userId2);
        if ((userOptional.isEmpty() || (userOptional2.isEmpty()))) {
            throw new DataNotFoundException("Объект не найден");
        }
        User user1 = userStorage.getUserById(userId1).get();
        User user2 = userStorage.getUserById(userId2).get();
        Set<Integer> first = user1.getFriends();
        Set<Integer> second = user2.getFriends();
        Set<Integer> result = first.stream().filter(element -> second.contains(element)).collect(Collectors.toSet());
        return result;
    }

    public User getById(int userId) {
        Optional<User> userOptional = userStorage.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        return userStorage.getUserById(userId).get();
    }
}
