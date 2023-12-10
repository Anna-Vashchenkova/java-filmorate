package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbGenreStorageTests {

    @Autowired
    private JdbcTemplate template;
    private DbGenreStorage storage;

    private final static String ACTION_MOVIE = "Боевик";
    private final static String TRAGED_MOVIE = "Трагеть";
    private final static String TRAGEDY_MOVIE = "Трагедия";

    @BeforeEach
    public void createStorage() {
        storage = new DbGenreStorage(template);
    }

    @Test
    @DisplayName("Должен возвращать жанр с идентификатором 1")
    public void shouldFindGenreWithId() {
        Optional<Genre> genre = storage.getById(1);
        assertEquals("Комедия", genre.get().getName());
    }

    @Test
    @DisplayName("После сохранения нового жанра, его можно найти по ID")
    public void findSavedGenre() {
        Genre genreToSave = Genre.builder()
                .name(ACTION_MOVIE)
                .build();
        int id = storage.create(genreToSave).getId();
        Optional<Genre> genre = storage.getById(id);
        assertTrue(genre.isPresent());
        assertEquals(ACTION_MOVIE, genre.get().getName());
    }

    @Test
    @DisplayName("Проверка удаления")
    public void deleteGenre() {
        Genre genreToSave = Genre.builder()
                .name(ACTION_MOVIE)
                .build();
        Genre genreToDelete = storage.create(genreToSave);
        storage.deleteGenre(genreToDelete);

        assertFalse(storage.getById(genreToDelete.getId()).isPresent());
    }

    @Test
    @DisplayName("Проверка обновления жанра")
    public void updateGenre() {
        Genre genreToSave = Genre.builder()
                .name(TRAGED_MOVIE)
                .build();
        Genre genre = storage.create(genreToSave);

        Genre genreToUpdate = Genre.builder()
                .id(genre.getId())
                .name(TRAGEDY_MOVIE)
                .build();
        storage.updateGenre(genreToUpdate);

        assertEquals(TRAGEDY_MOVIE, storage.getById(genre.getId()).get().getName());
    }

}