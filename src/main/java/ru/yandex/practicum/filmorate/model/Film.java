package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> likes = new HashSet<>();
    private Set<GenreEnum> genreEnums = new HashSet<>();
    private MpaRating mpa;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
                Set<Integer> likes, Set<GenreEnum> genreEnums, MpaRating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        if (likes != null) {
            this.likes = likes;
        }
        if (genreEnums != null) {
            this.genreEnums = genreEnums;
        }
        this.mpa = mpa;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Film film = (Film) object;

        return id == film.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}