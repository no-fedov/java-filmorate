package validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {
    private Validator validator;

    @BeforeEach
    public void set_up() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void check_User_EmailWithoutDog() {
        User user = User.builder()
                .id(0)
                .name("fsdf")
                .login("dsf")
                .email("dfdsf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_User_EmailIsEmpty() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login("login")
                .email("")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_User_EmailIsNull() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login("login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_User_NameIsEmpty() {
        User user = User.builder()
                .id(0)
                .name("")
                .login("dsf")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void check_User_LoginIsEmpty() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login("")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_User_LoginWithSpace() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login("   gfgf fgf")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_User_LoginNull() {
        User user = User.builder()
                .id(0)
                .name("name")
                .login(null)
                .email("gfgfg@dfdf")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_User_BirthdayInFuture() {
        User user = User.builder()
                .id(0)
                .name("")
                .login("login")
                .email("gfgfg@dfdf")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_User_BirthdayNull() {
        User user = User.builder()
                .id(0)
                .name("")
                .login("login")
                .email("gfgfg@dfdf")
                .birthday(null)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
