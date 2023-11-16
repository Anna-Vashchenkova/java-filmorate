package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Set;

@Slf4j
@RestController
public class FilmController {
    private FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Set<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping(value = "/films")
    public void delete(int filmId) {
        filmService.deleteFilm(filmId);
    }

    @PostMapping(value = "/films/{filmId}/likes/{userId}")
    public Set<Integer> addLikes(@PathVariable int userId, @PathVariable int filmId) {
        return filmService.addLikes(userId, filmId);
    }

    @DeleteMapping(value = "/films/{filmId}/likes/{userId}")
    public void deleteLikes(@PathVariable int userId, @PathVariable int filmId) {
        filmService.deleteLikes(userId, filmId);
    }

    @GetMapping("/films/popular?count={count}")
    public Set<Film> getTop10Films() {
        return  filmService.getTop10Films();
    }
}