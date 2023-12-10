package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component("databaseUS")
public class DbUserStorage implements UserStorage {
    public static final String INSERT_SQL = "insert into " +
            "users(name, login, birthday, email) " +
            "values (?,    ?,     ? ,      ?)";
    public static final String SELECT_ALL_SQL = "select * from users";
    public static final String SELECT_BY_ID_SQL = "select * from users where id = ?";
    public static final String DELETE_BY_ID_SQL = "delete from users where id = ?";
    public static final String SELECT_BY_EMAIL_SQL = "select * from users where email = ?";
    public static final String UPDATE_SQL = "update users set " +
            "name=? " +
            ", login=? " +
            ", birthday=? " +
            ", email=? " +
            "where id=?";

    private final JdbcTemplate jdbcTemplate;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setDate(3, Date.valueOf(user.getBirthday()));
            stmt.setString(4, user.getEmail());
            return stmt;
        }, keyHolder);
        return User.builder()
                .name(user.getName())
                .login(user.getLogin())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .id(Objects.requireNonNull(keyHolder.getKey()).intValue())
                .build();
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UPDATE_SQL,
                user.getName(),
                user.getLogin(),
                Date.valueOf(user.getBirthday()),
                user.getEmail(),
                user.getId());
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        return jdbcTemplate.query(SELECT_ALL_SQL, this::mapRowToModel);
    }

    @Override
    public void deleteUser(User user) {
        jdbcTemplate.update(DELETE_BY_ID_SQL, user.getId());

    }

    @Override
    public Optional<User> getUserById(int userId) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, this::mapRowToModel, userId).stream().findFirst();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return jdbcTemplate.query(SELECT_BY_EMAIL_SQL, this::mapRowToModel, email).stream().findFirst();
    }
    private User mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
