package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.Set;

@Component("databaseFS")
public class DbFilmStorage implements FilmStorage{
    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Set<Film> findAll() {
        return null;
    }

    @Override
    public void deleteFilm(Film film) {

    }

    @Override
    public Optional<Film> getById(int filmId) {
        return Optional.empty();
    }
}
