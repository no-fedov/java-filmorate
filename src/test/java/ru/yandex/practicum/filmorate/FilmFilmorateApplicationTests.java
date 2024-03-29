package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmFilmorateApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void check_PostFilm_WithEmptyName() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("cool film")
                .duration(100)
                .releaseDate(LocalDate.of(1995, 1, 1))
                .build();

        Film film1 = Film.builder()
                .name(null)
                .description("cool film")
                .duration(100)
                .releaseDate(LocalDate.of(1995, 1, 1))
                .build();

        Film film2 = Film.builder()
                .name("   ")
                .description("cool film")
                .duration(100)
                .releaseDate(LocalDate.of(1995, 1, 1))
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film1)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film2)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    public void check_PostFilm_DescriptionMoreThen200() throws Exception {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            string.append(i);
        }

        Film film = Film.builder()
                .name("COOL FILM")
                .description(string.toString())
                .duration(100)
                .releaseDate(LocalDate.of(1995, 1, 1))
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    public void check_PostFilm_DurationNegative() throws Exception {
        Film film = Film.builder()
                .name("COOL FILM")
                .description("cool")
                .duration(-100)
                .releaseDate(LocalDate.of(1995, 1, 1))
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    public void check_PostFilm_ReleaseDateIsBad() throws Exception {
        Film film = Film.builder()
                .name("Film")
                .description("description")
                .duration(100)
                .releaseDate(LocalDate.of(1000, 1, 1))
                .build();

        try {
            mockMvc.perform(post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(film)))
                    .andExpect(status().is4xxClientError());
        } catch (NestedServletException e) {
            assertTrue(e.getCause() instanceof ValidationException);
        }
    }
}
