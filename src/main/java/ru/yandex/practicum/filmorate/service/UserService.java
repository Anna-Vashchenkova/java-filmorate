package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(
            @Qualifier("databaseUS")
            UserStorage userStorage,
            FriendshipStorage friendshipStorage
    ) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
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
        if ((user.getName() == null) || (user.getName().isEmpty())) {
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
        if ((user.getName() == null) || (user.getName().isEmpty())) {
            user.setName(user.getLogin());
        }
        userUpdate.setName(user.getName());
        userUpdate.setBirthday(user.getBirthday());
        return userStorage.updateUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public void deleteUser(int userId) {
        Optional<User> optionalUser = userStorage.getUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("Пользователь не найден.");
        }
        userStorage.deleteUser(optionalUser.get());
    }

    public void deleteFriends(int userId, int friendsId) {
        if (friendshipStorage.findFriendshipBetween(userId, friendsId).isEmpty()) {
            throw new ValidationException("В списке друзей нет пользователя с таким id");
        } else {
            friendshipStorage.deleteFriendshipBetween(userId, friendsId);
        }
    }

    public List<User> getFriends(int userId) {
        getById(userId);
        List<Friendship> friendshipa = friendshipStorage.getFriendshipsForUser(userId);
        Set<Integer> ids = new HashSet<>();
        friendshipa.forEach(friendship -> {
            ids.add(friendship.getUserId());
            ids.add(friendship.getFriendId());
        });
        ids.remove(userId);
        return userStorage.findUsersByIds(ids);
    }

    private Set<User> userSetFromIdSet(Set<Integer> friends) {
        return friends.stream().map(this::getById).collect(Collectors.toSet());
    }

    public Set<User> commonFriends(int userId1, int userId2) {
        Optional<User> userOptional = userStorage.getUserById(userId1);
        Optional<User> userOptional2 = userStorage.getUserById(userId2);
        if ((userOptional.isEmpty() || (userOptional2.isEmpty()))) {
            throw new DataNotFoundException("Объект не найден");
        }
        List<User> friends1 = getFriends(userId1);
        List<User> friends2 = getFriends(userId2);
        return friends1.stream().filter(friends2::contains).collect(Collectors.toSet());
    }

    public User getById(int userId) {
        Optional<User> userOptional = userStorage.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        return userStorage.getUserById(userId).get();
    }

    public void addUser(int userId, int friendsId) {
        User user = getById(userId);
        User friend = getById(friendsId);
        if (friendshipStorage.findFriendshipBetween(userId, friendsId).isPresent()) {
            return;
        }
        Friendship friendship = Friendship.builder()
                .userId(userId)
                .friendId(friendsId)
                .friendshipStatus(FriendshipStatus.PENDING)
                .build();
        Friendship saved = friendshipStorage.create(friendship);
    }
}
