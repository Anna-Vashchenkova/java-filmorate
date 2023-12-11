package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DbFriendshipStorage implements FriendshipStorage {

    private static final String SELECT_BETWEEN_SQL = "SELECT * FROM FRIENDS WHERE " +
            "( (USER_ID = ?) AND (FRIEND_ID=?) ) OR " +
            "( (FRIEND_ID = ?) AND (USER_ID=?) )";
    private static final String DELETE_BETWEEN_SQL = "DELETE FROM FRIENDS WHERE " +
            "( (USER_ID = ?) AND (FRIEND_ID=?) ) OR " +
            "( (FRIEND_ID = ?) AND (USER_ID=?) )";
    private static final String INSERT_SQL =
            "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) " +
                    "VALUES (           ?,          ?,          ?  )";
    public static final String SELECT_BY_ID_SQL = "SELECT * FROM FRIENDS where ID = ?";
    public static final String SELECT_BY_USER_ID_SQL = "SELECT * FROM FRIENDS where USER_ID = ? OR FRIEND_ID = ?";
    private final JdbcTemplate jdbcTemplate;

    public DbFriendshipStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Friendship> findFriendshipBetween(int userId, int friendsId) {
        return jdbcTemplate.query(SELECT_BETWEEN_SQL, this::mapRowToModel,
                userId, friendsId, userId, friendsId).stream().findFirst();
    }

    @Override
    public Friendship create(Friendship friendship) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    INSERT_SQL,
                    new String[]{"id"}
            );
            stmt.setInt(1, friendship.getUserId());
            stmt.setInt(2, friendship.getFriendId());
            stmt.setString(3, friendship.getFriendshipStatus().name());
            return stmt;
        }, keyHolder);
        return getById(keyHolder.getKey().intValue()).get();
    }

    @Override
    public List<Friendship> getFriendshipsForUser(int userId) {
        return jdbcTemplate.query(
                SELECT_BY_USER_ID_SQL,
                this::mapRowToModel, userId, userId
        );
    }

    @Override
    public void deleteFriendshipBetween(int userId, int friendsId) {
        jdbcTemplate.update(DELETE_BETWEEN_SQL,userId, friendsId, userId, friendsId);
    }

    private Optional<Friendship> getById(int id) {
        return jdbcTemplate.query(
                SELECT_BY_ID_SQL,
                this::mapRowToModel, id
        ).stream().findFirst();
    }

    private Friendship mapRowToModel(ResultSet resultSet, int rowNum) throws SQLException {
        return Friendship.builder()
                .id(resultSet.getInt("id"))
                .userId(resultSet.getInt("user_id"))
                .friendId(resultSet.getInt("friend_id"))
                .friendshipStatus(FriendshipStatus.valueOf(resultSet.getString("friendship_status")))
                .build();
    }
}
