package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

@Component
public class InMemoryUserStorage implements UserStorage{
    @Override
    public User create(User user) {
        return null;
    }
    @Override
    public User updateUser(User user) {
        return  null;
    }

    @Override
    public Set<User> getUsers() {
        return  null;
    }
    @Override
    public void deleteUser(User user) {

    }
}
