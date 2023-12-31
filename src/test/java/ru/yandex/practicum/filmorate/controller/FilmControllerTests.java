package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTests {

    private final FilmController controller = new FilmController(
            new FilmService(new InMemoryFilmStorage(), new UserService(new InMemoryUserStorage())));

    @DisplayName("При сохранении фильма с пустым именем необходимо вернуть ошибку")
    @Test
    public void saveFilmWithEmptyName() {
        Set<Integer> likes = new HashSet<>();
        likes.add(0);
        likes.add(1);
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.COMEDY);
        Film film = new Film(1, "", "---", LocalDate.of(2023, 1, 15), 90, likes, genres, Rating.G);

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(film));
        assertEquals("не выполнены условия: название не может быть пустым;\n" +
                "    максимальная длина описания — 200 символов;\n" +
                "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                "    продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @DisplayName("При сохранении фильма с датой релиза раньше 28 декабря 1895 года необходимо вернуть ошибку")
    @Test
    public void saveFilmWithNotTrueReleaseDate() {
        Set<Integer> likes = new HashSet<>();
        likes.add(0);
        likes.add(1);
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.COMEDY);
        Film film = new Film(1, "kino", "---", LocalDate.of(1895, 12, 20), 90, likes, genres, Rating.G);

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(film));
        assertEquals("не выполнены условия: название не может быть пустым;\n" +
                "    максимальная длина описания — 200 символов;\n" +
                "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                "    продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @DisplayName("При сохранении фильма с продолжительностью меньше 0 необходимо вернуть ошибку")
    @Test
    public void saveFilmWithNotTrueDuration() {
        Set<Integer> likes = new HashSet<>();
        likes.add(0);
        likes.add(1);
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.COMEDY);
        Film film = new Film(1, "kino", "---", LocalDate.of(2000, 12, 20), -10, likes, genres, Rating.G);

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(film));
        assertEquals("не выполнены условия: название не может быть пустым;\n" +
                "    максимальная длина описания — 200 символов;\n" +
                "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                "    продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @DisplayName("При сохранении фильма с длиной описания более 200 символов нужно вернуть ошибку")
    @Test
    public void saveFilmWithLengthOfDescriptionGrater200chars() {
        String description = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, " +
                "sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. " +
                "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut " +
                "aliquip ex ea commodo consequat. Duis autem vel eum iriure d";
        Set<Integer> likes = new HashSet<>();
        likes.add(0);
        likes.add(1);
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.COMEDY);
        Film film = new Film(1, "kino", description, LocalDate.of(2000, 12, 20), -10, likes, genres, Rating.G);

        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(film));
        assertEquals("не выполнены условия: название не может быть пустым;\n" +
                "    максимальная длина описания — 200 символов;\n" +
                "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                "    продолжительность фильма должна быть положительной", exception.getMessage());
    }

    @DisplayName("При обновлении фильма с несуществующим id нужно вернуть ошибку")
    @Test
    public void updateFilmWithNonExistingId() {
        Set<Integer> likes = new HashSet<>();
        likes.add(0);
        likes.add(1);
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.COMEDY);
        Film film = new Film(999, "kino2", "---",
                LocalDate.of(2023, 10, 1), 60, likes, genres, Rating.G);

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            controller.updateFilm(film);
        });
        assertEquals("фильма с таким Id не существует", exception.getMessage());
    }
}