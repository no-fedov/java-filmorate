package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(int id1, int id2);

    void deleteFriend(int id1, int id2);

    List<User> getUserFriends(int id);

    List<User> getMutualFriendsList(int id1, int id2);
}
