package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private User user;


    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1)
                .name("java")
                .email("developer@mail.ru")
                .login("baobab")
                .birthday(LocalDate.of(1999, 12, 1))
                .build();
    }

    @Test
    public void check_addUser_shouldAddUser() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User newUser = userDbStorage.addUser(user);
        assertEquals(user, newUser);
    }

    @Test
    public void check_updateUser_shouldUpdate() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User newUser = user.toBuilder().birthday(LocalDate.of(2000, 11, 11)).build();
        userDbStorage.addUser(user);
        User updatedUser = userDbStorage.updateUser(newUser);
        assertEquals(newUser, updatedUser);
    }

    @Test
    public void check_findUser_shouldFind() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        userDbStorage.addUser(user);
        User findUser = userDbStorage.findUser(1).get();
        assertEquals(user, findUser);
    }

    @Test
    public void check_deleteUser_shouldDelete() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        userDbStorage.addUser(user);
        User deletedUser = userDbStorage.deleteUser(1);
        assertEquals(user, deletedUser);
    }

    @Test
    public void check_getAllUsers_shouldReturnListSize_3() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User user2 = user.toBuilder().name("Alex").email("refds@mail.ru").build();
        User user3 = user.toBuilder().name("loky").email("sdgfsdgafdg@mail.ru").build();

        userDbStorage.addUser(user);
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);

        int size = userDbStorage.getAllUser().size();

        assertEquals(3, size);
    }

    @Test
    public void check_user1_Friendship_to_user2() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        FriendshipDbStorage friendshipDbStorage = new FriendshipDbStorage(jdbcTemplate);
        User user2 = user.toBuilder().name("Alex").email("refds@mail.ru").build();

        User user1InDb = userDbStorage.addUser(user);
        User user2InDb = userDbStorage.addUser(user2);

        friendshipDbStorage.addFriend(1, 2);

        List<User> listFriendUser1 = friendshipDbStorage.getUserFriends(1);

        int countFriendUser1 = listFriendUser1.size();

        assertEquals(1, countFriendUser1);

        User friend = listFriendUser1.get(0);

        assertEquals(user2InDb, friend);

        int countFriendUser2 = friendshipDbStorage.getUserFriends(2).size();

        assertEquals(0, countFriendUser2);

        friendshipDbStorage.addFriend(2, 1);

        List<User> listFriendUser2 = friendshipDbStorage.getUserFriends(2);

        int size = listFriendUser2.size();

        assertEquals(1, size);

        assertEquals(user1InDb, listFriendUser2.get(0));
    }
}
