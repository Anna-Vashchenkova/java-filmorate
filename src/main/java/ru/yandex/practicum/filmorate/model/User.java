package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
    private Set<FriendRequest> friendRequests = new HashSet<>();

    public User(int id, String email, String login, String name, LocalDate birthday,
                Set<Integer> friends, Set<FriendRequest> friendRequests) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        if (friends != null) {
            this.friends = friends;
        }
        if (friendRequests != null) {
            this.friendRequests = friendRequests;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        User user = (User) object;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
