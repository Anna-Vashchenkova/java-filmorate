package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    Film create(Film film);

    Film updateFilm(Film film);

    Set<Film> findAll();

    void deleteFilm(Film film);

    Optional<Film> getById(int filmId);
}
