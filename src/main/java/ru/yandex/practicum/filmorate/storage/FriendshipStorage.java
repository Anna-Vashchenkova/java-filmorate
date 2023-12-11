package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipStorage {
    Optional<Friendship> findFriendshipBetween(int userId, int friendsId);

    Friendship create(Friendship friendship);

    List<Friendship> getFriendshipsForUser(int userId);

    void deleteFriendshipBetween(int userId, int friendsId);
}
