package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public Genre create(@RequestBody Genre genre) {
        return genreService.create(genre);
    }

    @GetMapping
    public List<Genre> getGenres(){
        return genreService.getGenres();
    }

    @GetMapping("/{genreId}")
    public Genre getGenres(@PathVariable int genreId){
        return genreService.getById(genreId);
    }

    @DeleteMapping("/{genreId}")
    public void deleteGenres(@PathVariable int genreId){
        genreService.delete(genreId);
    }

    @PutMapping
    public Genre updateGenre(@RequestBody Genre genre) {
        return genreService.updateGenre(genre);
    }
}
