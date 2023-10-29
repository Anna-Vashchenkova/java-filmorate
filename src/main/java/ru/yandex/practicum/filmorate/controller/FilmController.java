package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Set<Film> films = new HashSet<>();

    @GetMapping("/films")
    public Set<Film> findAll() {
        log.debug("Текущее количество постов: {}", films.size());
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        if ((film.getName().isEmpty()) || (film.getDescription().length() > 200)
                || (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) || (film.getDuration() < 0)) {
            throw new ValidationException("не выполнены условия: название не может быть пустым;\n" +
                    "    максимальная длина описания — 200 символов;\n" +
                    "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                    "    продолжительность фильма должна быть положительной");
        }
        films.add(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
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
            return film;
        } else {
            throw new ValidationException("не выполнены условия: название не может быть пустым;\n" +
                    "    максимальная длина описания — 200 символов;\n" +
                    "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                    "    продолжительность фильма должна быть положительной");
        }
    }
}