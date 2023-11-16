package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Set<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        if ((film.getName().isEmpty()) || (film.getDescription().length() > 200)
                || (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) || (film.getDuration() < 0)) {
            throw new ValidationException("не выполнены условия: название не может быть пустым;\n" +
                    "    максимальная длина описания — 200 символов;\n" +
                    "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                    "    продолжительность фильма должна быть положительной");
        }
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        if ((!film.getName().isEmpty()) && (film.getDescription().length() < 200)
                && (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
                && (film.getDuration() > 0)) {
            Optional<Film> optionalFilm = findAll().stream().filter(film1 -> film1.getId() == film.getId()).findFirst();
            if (optionalFilm.isEmpty()) {
                throw new NotFoundException("фильма с таким Id не существует");
            }
            Film filmUpdate = optionalFilm.get();
            filmUpdate.setName(film.getName());
            filmUpdate.setDescription(film.getDescription());
            filmUpdate.setReleaseDate(film.getReleaseDate());
            filmUpdate.setDuration(film.getDuration());
            filmUpdate.setLikes(film.getLikes());
            return filmStorage.updateFilm(film);
        } else {
            throw new ValidationException("не выполнены условия: название не может быть пустым;\n" +
                    "    максимальная длина описания — 200 символов;\n" +
                    "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                    "    продолжительность фильма должна быть положительной");
        }
    }

    public void deleteFilm(int filmId) {
        Optional<Film> optionalFilm = filmStorage.getById(filmId);
        if (optionalFilm.isEmpty()) {
            throw new NotFoundException("Фильм не найден.");
        }
        Film film = optionalFilm.get();
        filmStorage.deleteFilm(film);
    }
    public Set<Integer> addLikes(int userId, int filmId) {
        User user = userService.getById(userId);
        Optional<Film> optionalFilm = filmStorage.getById(filmId);
        if ((optionalFilm.isEmpty()) || (user == null)) {
            throw new NotFoundException("Объект не найден.");
        }
        Film film = optionalFilm.get();
        Set<Integer> likes = film.getLikes();
        if (likes.contains(user.getId())) {
            return likes;
        }
        likes.add(user.getId());
        return likes;
    }

    public void deleteLikes(int userId, int filmId) {
        Optional<Film> optionalFilm = filmStorage.getById(filmId);
        User user = userService.getById(userId);
        if ((optionalFilm.isEmpty()) || (user == null)) {
            throw new NotFoundException("Объект не найден.");
        }
        Film film = optionalFilm.get();
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