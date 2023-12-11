package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Film create(Film film);

    Film updateFilm(Film film);

    Collection<Film> findAll();

    void deleteFilm(Film film);

    Optional<Film> getById(int filmId);

    void addLike(int filmId, int userId);
}
