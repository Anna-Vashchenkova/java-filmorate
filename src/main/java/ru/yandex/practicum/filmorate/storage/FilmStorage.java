package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Film updateFilm(Film film);

    List<Film> findAll();

    void deleteFilm(Film film);

    Optional<Film> getById(int filmId);

    void addLike(int filmId, int userId);
}
