package validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {
    Validator validator;

    @BeforeEach
    public void set_up() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void check_Film_NameIsNull() {
        Film film = Film.builder()
                .id(1)
                .name(null)
                .description("Описание")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_Film_NameIsEmpty() {
        Film film = Film.builder()
                .id(1)
                .name("   ")
                .description("Описание")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_Film_DescriptionFilmMoreThen200() {
        StringBuilder description = new StringBuilder();

        for (int i = 1; i <= 200; i++) {
            description = description.append(i);
        }

        String filmDescription = description.toString();

        Film film = Film.builder()
                .id(1)
                .name("Буря в стакане")
                .description(filmDescription)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_Film_DescriptionIsBlank() {
        Film film = Film.builder()
                .id(1)
                .name("Буря в стакане")
                .description("")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_Film_DateFilm_isBefore_1895_12_28() {
        Film film = Film.builder()
                .id(1)
                .name("Буря в стакане")
                .description("filmDescription")
                .releaseDate(LocalDate.of(1894, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void check_Film_Duration_less_0() {
        Film film = Film.builder()
                .id(1)
                .name("Буря в стакане")
                .description("filmDescription")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(-1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
