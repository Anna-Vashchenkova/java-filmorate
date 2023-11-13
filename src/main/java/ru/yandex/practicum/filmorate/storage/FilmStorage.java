package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {
    public Film create(Film film);
    public Film updateFilm(Film film);
    public Set<Film> findAll();
    public void deleteFilm(Film film);
}
