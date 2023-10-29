package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTests {

    private final FilmController controller = new FilmController();

    @DisplayName("При сохранении фильма с пустым именем необходимо вернуть ошибку")
    @Test
    public void saveFilmWithEmptyName() {
        Film f = new Film(1, "", "---", LocalDate.of(2023, 1, 15), 90);
        ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                controller.create(f);
            }
        });
        assertEquals("не выполнены условия: название не может быть пустым;\n" +
                     "    максимальная длина описания — 200 символов;\n" +
                     "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                     "    продолжительность фильма должна быть положительной", exception.getMessage());

    }

    @DisplayName("При сохранении фильма с датой релиза раньше 28 декабря 1895 года необходимо вернуть ошибку")
    @Test
    public void saveFilmWithNotTrueReleaseDate() {
        Film f = new Film(1, "kino", "---", LocalDate.of(1895, 12, 20), 90);
        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(f));
        assertEquals("не выполнены условия: название не может быть пустым;\n" +
                     "    максимальная длина описания — 200 символов;\n" +
                     "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                     "    продолжительность фильма должна быть положительной", exception.getMessage());

    }

    @DisplayName("При сохранении фильма с продолжительностью меньше 0 необходимо вернуть ошибку")
    @Test
    public void saveFilmWithNotTrueDuration() {
        Film f = new Film(1, "kino", "---", LocalDate.of(2000, 12, 20), -10);
        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(f));
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
        Film f = new Film(1, "kino", description, LocalDate.of(2000, 12, 20), -10);
        ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(f));
        assertEquals("не выполнены условия: название не может быть пустым;\n" +
                     "    максимальная длина описания — 200 символов;\n" +
                     "    дата релиза — не раньше 28 декабря 1895 года;\n" +
                     "    продолжительность фильма должна быть положительной", exception.getMessage());
    }
}