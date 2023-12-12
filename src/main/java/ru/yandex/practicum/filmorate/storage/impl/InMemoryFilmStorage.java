package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Component("inMemoryFS")
public class InMemoryFilmStorage implements FilmStorage {
    private final Set<Film> films = new HashSet<>();
    private int lastId = 0;

    @Override
    public Film create(Film film) {
        film.setId(++lastId);
        films.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.add(film);
        return film;
    }

    @Override
    public List<Film> findAll() {
        log.debug("Текущее количество постов: {}", films.size());
        List<Film> films1 = new ArrayList<>();
        films1.addAll(films);
        return films1;
    }

    @Override
    public void deleteFilm(Film film) {
        films.remove(film);
    }

    @Override
    public Optional<Film> getById(int filmId) {
        return films.stream()
                .filter(film -> film.getId() == filmId)
                .findFirst();
    }

    @Override
    public void addLike(int filmId, int userId) {
        getById(filmId).get().getLikes().add(userId);
    }
}
