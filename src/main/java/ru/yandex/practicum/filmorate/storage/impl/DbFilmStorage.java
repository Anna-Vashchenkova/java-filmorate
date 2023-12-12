package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("databaseFS")
public class DbFilmStorage implements FilmStorage {
    public static final String INSERT_SQL = "insert into " +
            "films(name, description, release_date, duration, rating_id) " +
            "values (?,     ?,          ? ,         ?,          ?)";
    public static final String FILM_SELECT = "select " +
            "F.ID, F.NAME, F.DESCRIPTION, F.DURATION, F.RELEASE_DATE, M.ID as rating_id, M.NAME as rating_name " +
            "from films as F " +
            "join PUBLIC.MPAS M on M.ID = F.RATING_ID";
    public static final String SELECT_GENRES_SQL = "select GENRES.ID, GENRES.NAME from GENRES " +
            "join PUBLIC.FILM_GENRES FG on GENRES.ID = FG.GENRE_ID " +
            "where FG.FILM_ID = ? " +
            "order by GENRE_ID";
    public static final String SELECT_ALL_SQL = FILM_SELECT;
    public static final String SELECT_BY_ID_SQL = FILM_SELECT +
            " where F.ID = ?";

    public static final String DELETE_BY_ID_SQL = "delete from films where id = ?";
    public static final String DELETE_GENRES_BY_ID_SQL = "delete from film_genres where film_id = ?";
    public static final String INSERT_GENRES_SQL = "insert into film_genres(film_id, genre_id) values(?, ?)";
    public static final String UPDATE_SQL = "update films set " +
            "name=? " +
            ", description=? " +
            ", release_date=? " +
            ", duration=? " +
            ", rating_id=? " +
            "where id=?";
    public static final String INSERT_LIKE_SQL =
            "INSERT INTO LIKES (FILM_ID, USER_ID) " +
                    "values (       ?,      ? )";

    public static final String SELECT_LIKES_SQL = "SELECT * FROM LIKES WHERE FILM_ID=?";
    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        updateGenres(filmId, film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
        return Film.builder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .genres(loadGenres(filmId))
                .id(filmId)
                .build();
    }

    private List<Genre> loadGenres(int filmId) {
        return jdbcTemplate.query(SELECT_GENRES_SQL, this::mapRowToGenre, filmId);
    }

    private void updateGenres(int filmId, List<Integer> genresIds) {
        jdbcTemplate.update(DELETE_GENRES_BY_ID_SQL, filmId);
        new HashSet<>(genresIds).forEach(
                genreId -> jdbcTemplate.update(INSERT_GENRES_SQL, filmId, genreId)
        );
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(UPDATE_SQL,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        updateGenres(film.getId(), film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
        return getById(film.getId()).get();
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, this::mapRowToModel);
    }

    @Override
    public void deleteFilm(Film film) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, film.getId());
    }

    @Override
    public Optional<Film> getById(int filmId) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, this::mapRowToModel, filmId).stream().findFirst();
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update(INSERT_LIKE_SQL, filmId, userId);
    }

    private Set<Integer> loadLikes(int filmId) {
        return new HashSet<>(jdbcTemplate.query(SELECT_LIKES_SQL, this::mapRowToIntegers, filmId));
    }

    private Integer mapRowToIntegers(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getInt("USER_ID");
    }

    private Film mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        int filmId = resultSet.getInt("id");
        return Film.builder()
                .id(filmId)
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(
                        MpaRating.builder()
                                .id(resultSet.getInt("rating_id"))
                                .name(resultSet.getString("rating_name"))
                                .build()
                )
                .likes(loadLikes(filmId))
                .genres(loadGenres(filmId))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
