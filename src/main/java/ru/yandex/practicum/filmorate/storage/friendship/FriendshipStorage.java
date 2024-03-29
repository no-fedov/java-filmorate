package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(int id_1, int id_2);

    void deleteFriend(int id_1, int id_2);

    List<User> getUserFriends(int id);

    List<User> getMutualFriendsList(int id_1, int id_2);
}
