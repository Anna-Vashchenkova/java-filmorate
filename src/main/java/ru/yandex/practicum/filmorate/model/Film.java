package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
                Set<Integer> likes, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        if (likes != null) {
            this.likes = likes;
        }
        if (genres != null) {
            this.genres = genres;
        }
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