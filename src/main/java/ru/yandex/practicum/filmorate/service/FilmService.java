package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(
            @Qualifier("inMemoryFS")
            FilmStorage filmStorage,
            UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> findAll() {
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
                throw new DataNotFoundException("фильма с таким Id не существует");
            }
            Film filmUpdate = optionalFilm.get();
            filmUpdate.setName(film.getName());
            filmUpdate.setDescription(film.getDescription());
            filmUpdate.setReleaseDate(film.getReleaseDate());
            filmUpdate.setDuration(film.getDuration());
            filmUpdate.setLikes(film.getLikes());
            return filmStorage.updateFilm(film);
        }
        throw new ValidationException("не выполнены условия: название не может быть пустым;\n" +
                "    максимальная длина описания — 200 символов;\n" +
                "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                "    продолжительность фильма должна быть положительной");
    }

    public void deleteFilm(int filmId) {
        Film film = getById(filmId);
        filmStorage.deleteFilm(film);
    }

    public Film getById(int filmId) {
        Optional<Film> optionalFilm = filmStorage.getById(filmId);
        if (optionalFilm.isEmpty()) {
            throw new DataNotFoundException("Фильм не найден.");
        }
        return optionalFilm.get();
    }

    public Set<Integer> addLikes(int userId, int filmId) {
        User user = userService.getById(userId);
        Optional<Film> optionalFilm = filmStorage.getById(filmId);
        if ((optionalFilm.isEmpty()) || (user == null)) {
            throw new DataNotFoundException("Объект не найден.");
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
            throw new DataNotFoundException("Объект не найден.");
        }
        Film film = optionalFilm.get();
        Set<Integer> likes = film.getLikes();
        if (likes.contains(user.getId())) {
            likes.remove(user.getId());
        } else {
            throw new ValidationException("Пользователь не ставил лайк фильму.");
        }
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator
                        .comparingInt(film -> (((Film) film).getLikes().size() * -1))
                        .thenComparing(film -> ((Film)film).getName()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
