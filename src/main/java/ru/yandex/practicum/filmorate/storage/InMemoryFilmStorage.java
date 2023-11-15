package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
        if ((film.getName().isEmpty()) || (film.getDescription().length() > 200)
                || (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) || (film.getDuration() < 0)) {
            throw new ValidationException("не выполнены условия: название не может быть пустым;\n" +
                    "    максимальная длина описания — 200 символов;\n" +
                    "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                    "    продолжительность фильма должна быть положительной");
        }
        film.setId(++lastId);
        films.add(film);
        return film;
    }
    @Override
    public Film updateFilm(Film film) {
        if ((!film.getName().isEmpty()) && (film.getDescription().length() < 200)
                && (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)))
                && (film.getDuration() > 0)) {
            Optional<Film> optionalFilm = findAll().stream().filter(film1 -> film1.getId() == film.getId()).findFirst();
            if (optionalFilm.isEmpty()) {
                throw new ValidationException("фильма с таким Id не существует");
            }
            Film filmUpdate = optionalFilm.get();
            filmUpdate.setName(film.getName());
            filmUpdate.setDescription(film.getDescription());
            filmUpdate.setReleaseDate(film.getReleaseDate());
            filmUpdate.setDuration(film.getDuration());
            filmUpdate.setLikes(film.getLikes());
            return film;
        } else {
            throw new ValidationException("не выполнены условия: название не может быть пустым;\n" +
                    "    максимальная длина описания — 200 символов;\n" +
                    "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                    "    продолжительность фильма должна быть положительной");
        }
    }

    @Override
    public Set<Film> findAll() {
        log.debug("Текущее количество постов: {}", films.size());
        return films;
    }
    @Override
    public void deleteFilm(Film film) {
        if (films.contains(film)) {
            films.remove(film);
        } else {
            throw new ValidationException("Фильм не найден.");
        }
    }

    @Override
    public Film getById(int filmId) {
        return films.stream().filter(film -> film.getId() == filmId).findFirst().get();
    }
}
