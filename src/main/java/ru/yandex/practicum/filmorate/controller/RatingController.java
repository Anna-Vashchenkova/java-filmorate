package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/mpa")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService genreService) {
        this.ratingService = genreService;
    }

    @PostMapping
    public MpaRating create(@RequestBody MpaRating rating) {
        return ratingService.create(rating);
    }

    @GetMapping
    public List<MpaRating> getGenres() {
        return ratingService.getRatings();
    }

    @GetMapping("/{ratingId}")
    public MpaRating getGenres(@PathVariable int ratingId) {
        return ratingService.getById(ratingId);
    }

    @DeleteMapping("/{ratingId}")
    public void deleteGenres(@PathVariable int ratingId) {
        ratingService.delete(ratingId);
    }

    @PutMapping
    public MpaRating updateGenre(@RequestBody MpaRating rating) {
        return ratingService.updateRating(rating);
    }
}
