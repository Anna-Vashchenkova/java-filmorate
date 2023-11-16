package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Set<Film> films = new HashSet<>();
    private int lastId = 0;

    @Override
    public Film create(Film film){
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
    public Set<Film> findAll() {
        log.debug("Текущее количество постов: {}", films.size());
        return films;
    }
    @Override
    public void deleteFilm(Film film) {
        films.remove(film);
    }

    @Override
    public Optional<Film> getById(int filmId) {
        return films.stream().filter(film -> film.getId() == filmId).findFirst();
    }
}
