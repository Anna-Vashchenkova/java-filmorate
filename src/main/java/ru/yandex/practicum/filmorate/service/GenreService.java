package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Component
public class GenreService {

    private final GenreStorage storage;

    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public Genre create(Genre genre) {
        if (storage.getByName(genre.getName()).isPresent()) {
            throw new ValidationException("Жанр с таким именем уже существует");
        }
        return storage.create(genre);
    }

    public List<Genre> getGenres() {
        return storage.findAll();
    }

    public Genre updateGenre(Genre genre) {
        Optional<Genre> genreOptional = storage.getById(genre.getId());
        if (genreOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        Optional<Genre> otherGenre = storage.getByName(genre.getName());
        if ((otherGenre.isPresent()) && (otherGenre.get().getId() != genre.getId())) {
            throw new ValidationException("Жанр с таким именем уже существует");
        }
        return storage.updateGenre(genre);
    }

    public Genre getById(int genreId) {
        Optional<Genre> genreOptional = storage.getById(genreId);
        if (genreOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        return genreOptional.get();
    }

    public void delete(int genreId) {
        Optional<Genre> genreOptional = storage.getById(genreId);
        if (genreOptional.isEmpty()) {
            throw new DataNotFoundException("Объект не найден");
        }
        storage.deleteGenre(genreOptional.get());
    }
}
