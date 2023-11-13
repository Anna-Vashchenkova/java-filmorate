package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage{
    protected final Set<User> users = new HashSet<>();
    protected int lastId = 0;

    @Override
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
        } else if (users.contains(user)) {
            throw new ValidationException("пользователь с указанным адресом электронной почты уже был добавлен ранее");
        }
        user.setId(++lastId);
        users.add(user);
        return user;
    }
    @Override
    public User updateUser(User user) {
        if (user.getEmail().isEmpty()) {
            throw new ValidationException("в переданных данных отсутствует адрес электронной почты");
        } else {
            Optional<User> optionalUser = getUsers().stream().filter(user1 -> user1.getId() == user.getId()).findFirst();
            if (optionalUser.isEmpty()) {
                throw new ValidationException("пользователя с таким Id не существует");
            }
            User userUpdate = optionalUser.get();
            userUpdate.setEmail(user.getEmail());
            userUpdate.setLogin(user.getLogin());
            userUpdate.setName(user.getName());
            userUpdate.setBirthday(user.getBirthday());
            return user;
        }
    }

    @Override
    public Set<User> getUsers() {
        return users;
    }
    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }
}
