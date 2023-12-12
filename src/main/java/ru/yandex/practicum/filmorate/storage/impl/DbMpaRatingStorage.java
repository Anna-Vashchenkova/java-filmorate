package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DbMpaRatingStorage implements MpaRatingStorage {

    public static final String INSERT_SQL = "insert into mpas(name) values (?)";
    public static final String SELECT_ALL_SQL = "select * from mpas";
    public static final String SELECT_BY_ID_SQL = "select * from mpas where id = ?";
    public static final String DELETE_BY_ID_SQL = "delete from mpas where id = ?";
    public static final String SELECT_BY_NAME_SQL = "select * from mpas where name = ?";
    public static final String UPDATE_SQL = "update mpas set name=? where id=?";

    private final JdbcTemplate jdbcTemplate;

    public DbMpaRatingStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating create(MpaRating genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        return MpaRating.builder()
                .name(genre.getName())
                .id(Objects.requireNonNull(keyHolder.getKey()).intValue())
                .build();
    }

    @Override
    public MpaRating updateRating(MpaRating genre) {
        jdbcTemplate.update(UPDATE_SQL,
                genre.getName(),
                genre.getId());
        return genre;
    }

    @Override
    public List<MpaRating> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, this::mapRowToModel);
    }

    @Override
    public void deleteRating(MpaRating genre) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, genre.getId());
    }

    @Override
    public Optional<MpaRating> getById(int genreId) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, this::mapRowToModel, genreId).stream().findFirst();
    }

    @Override
    public Optional<MpaRating> getByName(String name) {
        return jdbcTemplate.query(SELECT_BY_NAME_SQL, this::mapRowToModel, name).stream().findFirst();
    }

    private MpaRating mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
