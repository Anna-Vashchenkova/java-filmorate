package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Set<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(int filmId) {
        Film film = filmStorage.getById(filmId);
        filmStorage.deleteFilm(film);
    }
    public Set<Integer> addLikes(int userId, int filmId) {
        User user = userService.getById(userId);
        Film film = filmStorage.getById(filmId);
        Set<Integer> likes = film.getLikes();
        if (likes.contains(user.getId())) {
            return likes;
        }
        likes.add(user.getId());
        return likes;
    }

    public void deleteLikes(int userId, int filmId) {
        User user = userService.getById(userId);
        Film film = filmStorage.getById(filmId);
        Set<Integer> likes = film.getLikes();
        if (likes.contains(user.getId())) {
            likes.remove(user.getId());
        } else {
            throw new ValidationException("Пользователь не ставил лайк фильму.");
        }
    }

    public Set<Film> getTop10Films() {
        Set<Film> all = filmStorage.findAll();
        List<Film> toSort = new ArrayList<>();
        for (Film film : all) {
            toSort.add(film);
        }
        toSort.sort((o1, o2) -> {
            if (o1.getLikes().size() == o2.getLikes().size())
                return o1.getName().compareTo(o2.getName());
            else if (o1.getLikes().size() > o2.getLikes().size())
                return 1;
            else return -1;
        });
        Set<Film> result = new HashSet<>();
        for (Film film : toSort) {
            result.add(film);
        }
        return result;
    }
}
