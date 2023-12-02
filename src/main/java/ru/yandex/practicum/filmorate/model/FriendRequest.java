package ru.yandex.practicum.filmorate.model;

public class FriendRequest {
    private int userId;
    private FriendshipStatus friendshipStatus;

    public FriendRequest(int userId, FriendshipStatus friendshipStatus) {
        this.userId = userId;
        this.friendshipStatus = friendshipStatus;
    }
}
