package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTests {
    private UserController controller = new UserController();

    @DisplayName("При добавлении пользователя с пустым значение почты необходимо вернуть ошибку")
    @Test
    public void saveUserWithEmptyEmail() {
        User user = new User(1, "", "a777", "anabell", LocalDate.of(2000, 10, 12));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("в переданных данных электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя со значение почты без символа @ необходимо вернуть ошибку")
    @Test
    public void saveUserNotTrueEmail() {
        User user = new User(1, "anabell!mail.ru", "a777", "anabell", LocalDate.of(2000, 10, 12));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("в переданных данных электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя с пустым значением логина необходимо вернуть ошибку")
    @Test
    public void saveUserWithEmptyLogin() {
        User user = new User(1, "anabell@mail.ru", "", "anabell", LocalDate.of(2000, 10, 12));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя с значением логина с пробелами необходимо вернуть ошибку")
    @Test
    public void saveUserWithNotTrueLogin() {
        User user = new User(1, "anabell@mail.ru", "a 777", "anabell", LocalDate.of(2000, 10, 12));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя с значением даты рождения в будущем необходимо вернуть ошибку")
    @Test
    public void saveUserWithNotTrueBirthday() {
        User user = new User(1, "anabell@mail.ru", "a777", "anabell", LocalDate.of(2025, 10, 12));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.create(user);
        });
        assertEquals("дата рождения не может быть в будущем", exception.getMessage());
    }

    @DisplayName("При добавлении пользователя с пустым значением имени будет использован логин")
    @Test
    public void saveUserWithEmptyName() {
        User user = new User(1, "anabell@mail.ru", "a777", "", LocalDate.of(2000, 10, 12));
        controller.create(user);


        assertTrue(controller.getUsers().contains(user));
    }

}