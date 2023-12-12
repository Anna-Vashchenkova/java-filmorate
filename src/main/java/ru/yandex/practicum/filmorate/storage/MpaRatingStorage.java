package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaRatingStorage {
    MpaRating create(MpaRating genre);

    MpaRating updateRating(MpaRating genre);

    List<MpaRating> findAll();

    void deleteRating(MpaRating genre);

    Optional<MpaRating> getById(int genreId);

    Optional<MpaRating> getByName(String name);
}
