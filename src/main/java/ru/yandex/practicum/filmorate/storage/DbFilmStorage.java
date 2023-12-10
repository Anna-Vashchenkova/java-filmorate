package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component("databaseFS")
public class DbFilmStorage implements FilmStorage {
    public static final String INSERT_SQL = "insert into " +
            "films(name, description, release_date, duration, rating_id) " +
            "values (?,     ?,          ? ,         ?,          ?)";
    public static final String FILM_SELECT = "select " +
            "F.ID, F.NAME, F.DESCRIPTION, F.DURATION, F.RELEASE_DATE, M.ID as rating_id, M.NAME as rating_name " +
            "from films as F " +
            "join PUBLIC.MPAS M on M.ID = F.RATING_ID";
    public static final String SELECT_ALL_SQL = FILM_SELECT;
    public static final String SELECT_BY_ID_SQL = FILM_SELECT +
                " where F.ID = ?";

    public static final String DELETE_BY_ID_SQL = "delete from films where id = ?";
    public static final String SELECT_BY_EMAIL_SQL = "select * from films where email = ?";
    public static final String UPDATE_SQL = "update films set " +
            "name=? " +
            ", description=? " +
            ", release_date=? " +
            ", duration=? " +
            ", rating_id=? " +
            "where id=?";

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
        return Film.builder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .id(Objects.requireNonNull(keyHolder.getKey()).intValue())
                .build();
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
        return film;
    }

    @Override
    public Collection<Film> findAll() {
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

    private Film mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
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
                .build();
    }
}
