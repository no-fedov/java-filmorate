package unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.integration.controller.FilmController;
import ru.yandex.practicum.filmorate.integration.exception.ValidationException;
import ru.yandex.practicum.filmorate.integration.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void set_up() {
        filmController = new FilmController();
    }

    @Test
    public void check_PostFilm_EmptyName() {
        Film film = Film.builder()
                .id(1)
                .name(null)
                .description("Описание")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film));
        assertEquals(exception.getMessage(), "Название фильма не может быть пустым");
        assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    public void check_PostFilm_DescriptionFilmMoreThen200() {
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

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film));
        assertEquals(exception.getMessage(), "Описание фильма не может быть больше 200 символов");
        assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    public void check_PostFilm_DateFilm_isBefore_1895_12_28() {
        Film film = Film.builder()
                .id(1)
                .name("Буря в стакане")
                .description("filmDescription")
                .releaseDate(LocalDate.of(1894, 1, 1))
                .duration(100)
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film));
        assertEquals(exception.getMessage(), "Дата фильма не может быть раньше 28 декабря 1895 года");
        assertTrue(filmController.getFilms().isEmpty());
    }

    @Test
    public void check_PostFilm_Duration_less_0() {
        Film film = Film.builder()
                .id(1)
                .name("Буря в стакане")
                .description("filmDescription")
                .releaseDate(LocalDate.of(1900, 1, 1))
                .duration(-1)
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.postFilm(film));
        assertEquals(exception.getMessage(), "Продолжительность фильма не может быть отрицательной");
        assertTrue(filmController.getFilms().isEmpty());
    }
}
