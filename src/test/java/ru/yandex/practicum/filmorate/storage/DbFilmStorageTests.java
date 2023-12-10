package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DbFilmStorageTests {

    @Autowired
    private JdbcTemplate template;
    private DbFilmStorage storage;

    @BeforeEach
    public void createStorage() {
        storage = new DbFilmStorage(template);
    }

    @Test
    @DisplayName("Должен создавать фильм с рейтингом")
    public void createFilm() {
        Film filmToSave = Film.builder()
                .duration(14)
                .name("Пираты карибского моря")
                .description("История легендарного Джека Воробья... Капитана Джека Воробья")
                .releaseDate(LocalDate.of(2003, 7, 22))
                .mpa(MpaRating.builder()
                        .id(1)
                        .build())
                .build();
        Film film = storage.create(filmToSave);

        Film savedFilm = storage.getById(film.getId()).get();

        assertEquals(MpaRating.builder()
                .id(1)
                .name("G")
                .build(), savedFilm.getMpa());
    }
}