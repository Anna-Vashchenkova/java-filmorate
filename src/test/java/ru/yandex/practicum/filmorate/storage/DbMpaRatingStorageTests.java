package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbMpaRatingStorageTests {

    private static final String RATING_3_PLUS = "3+";
    private static final String RATING_6 = "6";
    private static final String RATING_6_PLUS = "6+";

    @Autowired
    private JdbcTemplate template;
    private MpaRatingStorage storage;

    @BeforeEach
    public void createStorage() {
        storage = new DbMpaRatingStorage(template);
    }

    @Test
    @DisplayName("Должен возвращать жанр с идентификатором 1")
    public void shouldFindGenreWithId() {
        Optional<MpaRating> rating = storage.getById(1);
        assertEquals("18+", rating.get().getName());
    }

    @Test
    @DisplayName("После сохранения нового жанра, его можно найти по ID")
    public void findSavedGenre() {
        MpaRating ratingToSave = MpaRating.builder()
                .name(RATING_3_PLUS)
                .build();
        int id = storage.create(ratingToSave).getId();
        Optional<MpaRating> raitng = storage.getById(id);
        assertTrue(raitng.isPresent());
        assertEquals(RATING_3_PLUS, raitng.get().getName());
    }

    @Test
    @DisplayName("Проверка удаления")
    public void deleteGenre() {
        MpaRating ratingToSave = MpaRating.builder()
                .name(RATING_3_PLUS)
                .build();
        MpaRating ratingToDelete = storage.create(ratingToSave);
        storage.deleteRating(ratingToDelete);

        assertFalse(storage.getById(ratingToDelete.getId()).isPresent());
    }

    @Test
    @DisplayName("Проверка обновления жанра")
    public void updateGenre() {
        MpaRating ratingToSave = MpaRating.builder()
                .name(RATING_6)
                .build();
        MpaRating rating = storage.create(ratingToSave);

        MpaRating ratingToUpdate = MpaRating.builder()
                .id(rating.getId())
                .name(RATING_6_PLUS)
                .build();
        storage.updateRating(ratingToUpdate);

        assertEquals(RATING_6_PLUS, storage.getById(rating.getId()).get().getName());
    }

}