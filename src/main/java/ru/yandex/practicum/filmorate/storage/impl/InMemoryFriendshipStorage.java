package ru.yandex.practicum.filmorate.storage.impl;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.List;
import java.util.Optional;

public class InMemoryFriendshipStorage implements FriendshipStorage {
    @Override
    public Optional<Friendship> findFriendshipBetween(int userId, int friendsId) {
        return Optional.empty();
    }

    @Override
    public Friendship create(Friendship friendship) {
        return null;
    }

    @Override
    public List<Friendship> getFriendshipsForUser(int userId) {
        return null;
    }

    @Override
    public void deleteFriendshipBetween(int userId, int friendsId) {

    }
}
