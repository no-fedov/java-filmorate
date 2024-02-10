package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void set_up() {
        userController = new UserController();
    }

    @Test
    public void check_PostUser_EmailWithoutDog() {
        User user = User.builder()
                .id(0)
                .name("fsdf")
                .login("dsf")
                .email("dfdsf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user));

        assertEquals(exception.getMessage(), "Неверный формат email");
        assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    public void check_PostUser_EmailIsEmpty() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login("login")
                .email(null)
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user));

        assertEquals(exception.getMessage(), "Неверный формат email");
        assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    public void check_PostUser_NameIsEmpty() {
        User user = User.builder()
                .id(0)
                .name("")
                .login("dsf")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userController.postUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void check_PostUser_LoginIsEmpty() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login("")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user));

        assertEquals(exception.getMessage(), "Логин не может быть пустым");
        assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    public void check_PostUser_LoginWithSpace() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login("   gfgf fgf")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user));

        assertEquals(exception.getMessage(), "Логин не может содержать пробелов");
        assertTrue(userController.getUsers().isEmpty());
    }

    @Test
    public void check_PostUser_BirthdayInFuture() {
        User user = User.builder()
                .id(0)
                .name("")
                .login("login")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.postUser(user));

        assertEquals(exception.getMessage(), "День рождения не может быть в будущем");
        assertTrue(userController.getUsers().isEmpty());
    }
}
