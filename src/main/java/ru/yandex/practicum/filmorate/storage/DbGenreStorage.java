package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DbGenreStorage implements GenreStorage {

    public static final String SELECT_ALL_SQL = "select * from genres";
    public static final String SELECT_BY_ID_SQL = "select * from genres where id = ?";
    public static final String DELETE_BY_ID_SQL = "delete from genres where id = ?";
    public static final String SELECT_BY_NAME_SQL = "select * from genres where name = ?";
    public static final String UPDATE_SQL = "update genres set name=? where id=?";

    private final JdbcTemplate jdbcTemplate;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre create(Genre genre) {
        String sqlQuery = "insert into genres(name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        return Genre.builder()
                .name(genre.getName())
                .id(Objects.requireNonNull(keyHolder.getKey()).intValue())
                .build();
    }

    @Override
    public Genre updateGenre(Genre genre) {
        jdbcTemplate.update(UPDATE_SQL,
                genre.getName(),
                genre.getId());
        return genre;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, this::mapRowToEmployee);
    }

    @Override
    public void deleteGenre(Genre genre) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, genre.getId());
    }

    @Override
    public Optional<Genre> getById(int genreId) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, this::mapRowToEmployee, genreId).stream().findFirst();
    }

    @Override
    public Optional<Genre> getByName(String name) {
        return jdbcTemplate.query(SELECT_BY_NAME_SQL, this::mapRowToEmployee, name).stream().findFirst();
    }

    private Genre mapRowToEmployee(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
