package dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;


@JdbcTest
@ContextConfiguration(classes = {FilmorateApplication.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private UserStorage userDbStorage;
    private FriendshipStorage friendshipStorage;

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

        userDbStorage = new UserDbStorage(jdbcTemplate);
        friendshipStorage = new FriendshipDbStorage(jdbcTemplate);
    }

    @Test
    public void check_addUser_shouldAddUser() {
        User newUser = userDbStorage.addUser(user);
        assertEquals(user, newUser);
    }

    @Test
    public void check_updateUser_shouldUpdate() {
        User newUser = user.toBuilder().birthday(LocalDate.of(2000, 11, 11)).build();
        userDbStorage.addUser(user);
        User updatedUser = userDbStorage.updateUser(newUser);
        assertEquals(newUser, updatedUser);
    }

    @Test
    public void check_findUser_shouldFind() {
        userDbStorage.addUser(user);
        User findUser = userDbStorage.findUser(1).get();
        assertEquals(user, findUser);
    }

    @Test
    public void check_deleteUser_shouldDelete() {
        userDbStorage.addUser(user);
        User deletedUser = userDbStorage.deleteUser(1);
        assertEquals(user, deletedUser);
    }

    @Test
    public void check_getAllUsers_shouldReturnListSize_3() {
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
        User user2 = user.toBuilder().name("Alex").email("refds@mail.ru").build();

        User user1InDb = userDbStorage.addUser(user);
        User user2InDb = userDbStorage.addUser(user2);

        friendshipStorage.addFriend(1, 2);

        List<User> listFriendUser1 = friendshipStorage.getUserFriends(1);

        int countFriendUser1 = listFriendUser1.size();

        assertEquals(1, countFriendUser1);

        User friend = listFriendUser1.get(0);

        assertEquals(user2InDb, friend);

        int countFriendUser2 = friendshipStorage.getUserFriends(2).size();

        assertEquals(0, countFriendUser2);

        friendshipStorage.addFriend(2, 1);

        List<User> listFriendUser2 = friendshipStorage.getUserFriends(2);

        int size = listFriendUser2.size();

        assertEquals(1, size);

        assertEquals(user1InDb, listFriendUser2.get(0));
    }
}
