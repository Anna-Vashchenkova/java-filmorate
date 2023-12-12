package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Genre create(Genre genre);

    Genre updateGenre(Genre genre);

    List<Genre> findAll();

    void deleteGenre(Genre genre);

    Optional<Genre> getById(int genreId);

    Optional<Genre> getByName(String name);
}
