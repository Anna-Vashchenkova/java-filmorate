package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    public Set<String> addFriends(User user, String email) {
        Set<String> userFriends = user.getFriends();
        if (userFriends.contains(email)) {
            return userFriends;
        }
        userFriends.add(email);
        return userFriends;
    }

    public void deleteFriends(User user, String email) {
        Set<String> userFriends = user.getFriends();
        if (userFriends.contains(email)) {
            userFriends.remove(email);
        } else {
            throw new ValidationException("В списке друзей нет пользователя с таким email");
        }
    }

    public Set<String> commonFriends(User user1, User user2) {
        Set<String> first = user1.getFriends();
        Set<String> second = user2.getFriends();
        Set<String> result = first.stream().filter(element -> second.contains(element)).collect(Collectors.toSet());
        return result;
    }
}
