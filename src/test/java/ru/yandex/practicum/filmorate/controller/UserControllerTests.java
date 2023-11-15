package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTests {
    private UserController controller = new UserController(new UserService(new InMemoryUserStorage()));

    @DisplayName("При добавлении пользователя с пустым значение почты необходимо вернуть ошибку")
    @Test
    public void saveUserWithEmptyEmail() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(1, "", "a777", "anabell", LocalDate.of(2000, 10, 12), friends);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("в переданных данных электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя со значение почты без символа @ необходимо вернуть ошибку")
    @Test
    public void saveUserNotTrueEmail() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(1, "anabell!mail.ru", "a777", "anabell", LocalDate.of(2000, 10, 12), friends);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("в переданных данных электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя с пустым значением логина необходимо вернуть ошибку")
    @Test
    public void saveUserWithEmptyLogin() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(1, "anabell@mail.ru", "", "anabell", LocalDate.of(2000, 10, 12), friends);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя с значением логина с пробелами необходимо вернуть ошибку")
    @Test
    public void saveUserWithNotTrueLogin() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(1, "anabell@mail.ru", "a 777", "anabell", LocalDate.of(2000, 10, 12), friends);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя с значением даты рождения в будущем необходимо вернуть ошибку")
    @Test
    public void saveUserWithNotTrueBirthday() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(1, "anabell@mail.ru", "a777", "anabell", LocalDate.of(2025, 10, 12), friends);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("дата рождения не может быть в будущем", exception.getMessage());
    }

    @DisplayName("При обновлении пользователя с несуществующем Id необходимо возвращать ошибку")
    @Test
    public void updateUserWithNonExistingId() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(9999, "anabell@mail.ru", "a777", "anabell", LocalDate.of(2000, 10, 12), friends);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.updateUser(user);
        });
        assertEquals("пользователя с таким Id не существует", exception.getMessage());
    }

    @DisplayName("При обновлении пользователя верно обновляются поля")
    @Test
    public void updateUser() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(0, "anabell_543@mail.ru", "a777", "anabell", LocalDate.of(2000, 10, 12), friends);
        User resultUser = controller.create(user);
        User userForUpdate = new User(resultUser.getId(), "anabell_888@mail.ru", "a888", "anabell888", LocalDate.of(2000, 9, 14), friends);

        controller.updateUser(userForUpdate);
        Set<User> users = controller.getUsers();
        Optional<User> optionalUser = users.stream().filter(user1 -> user1.getId() == userForUpdate.getId()).findFirst();
        assertTrue(optionalUser.isPresent());
        assertEquals(userForUpdate, optionalUser.get());
    }

    @DisplayName("При добавлении пользователя с пустым значением имени будет использован логин")
    @Test
    public void saveUserWithEmptyName() {
        Set<Integer> friends = new HashSet<>();
        friends.add(0);
        friends.add(1);
        User user = new User(1, "anabell@mail.ru", "a777", null, LocalDate.of(2000, 10, 12), friends);
        controller.create(user);

        assertTrue(controller.getUsers().contains(user));
    }
}