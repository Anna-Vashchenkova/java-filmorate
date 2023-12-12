package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@Component
public class RatingService {

    private final MpaRatingStorage storage;

    public RatingService(MpaRatingStorage storage) {
        this.storage = storage;
    }

    public MpaRating create(MpaRating rating) {
        if (storage.getByName(rating.getName()).isPresent()) {
            throw new ValidationException("Рейтинг с таким именем уже существует");
        }
        return storage.create(rating);
    }

    public List<MpaRating> getRatings() {
        return storage.findAll();
    }

    public MpaRating updateRating(MpaRating genre) {
        Optional<MpaRating> ratingOptional = storage.getById(genre.getId());
        if (ratingOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        Optional<MpaRating> otherRating = storage.getByName(genre.getName());
        if ((otherRating.isPresent()) && (otherRating.get().getId() != genre.getId())) {
            throw new ValidationException("Рейтинг с таким именем уже существует");
        }
        return storage.updateRating(genre);
    }

    public MpaRating getById(int genreId) {
        Optional<MpaRating> ratingOptional = storage.getById(genreId);
        if (ratingOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        return ratingOptional.get();
    }

    public void delete(int genreId) {
        Optional<MpaRating> ratingOptional = storage.getById(genreId);
        if (ratingOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        storage.deleteRating(ratingOptional.get());
    }
}
