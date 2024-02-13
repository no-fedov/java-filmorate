package ru.yandex.practicum.filmorate.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.integration.controller.FilmController;
import ru.yandex.practicum.filmorate.integration.model.Film;
import ru.yandex.practicum.filmorate.integration.validator.FilmValidator;

import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmFilmorateApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FilmController filmController;
    @MockBean
    private FilmValidator validationException;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
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
}
