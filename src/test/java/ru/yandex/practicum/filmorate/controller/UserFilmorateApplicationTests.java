package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserFilmorateApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void check_PostUser_WithBadEmail() throws Exception {
        User user = User.builder()
                .name("Andrei")
                .login("andry")
                .email("dsdsd")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        User user1 = User.builder()
                .name("Zhenya")
                .login("jhony")
                .email("")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        User user2 = User.builder()
                .name("Lolyta")
                .login("loly")
                .email(null)
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void check_PostUser_WithoutLogin() throws Exception {
        User user = User.builder()
                .name("Andrei")
                .login("")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        User user1 = User.builder()
                .name("Zhenya")
                .login("          ")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        User user2 = User.builder()
                .name("Lolyta")
                .login(null)
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void check_PostUser_WithEmptyName() throws Exception {
        User user = User.builder()
                .name("")
                .login("andrei")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        User user1 = User.builder()
                .name("  ")
                .login("zhenya")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        User user2 = User.builder()
                .name(null)
                .login("Lolyta")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isOk());
    }

    @Test
    public void check_PostUser_BirthDayInFuture() throws Exception {
        User user = User.builder()
                .name("")
                .login("andrei")
                .email("mail@yandex.ru")
                .birthday(LocalDate.of(2222, 1, 1))
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().is4xxClientError());
    }
}
