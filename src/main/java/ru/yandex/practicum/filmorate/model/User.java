package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    private final int id;

    @NotBlank
    private String name;

    @NotBlank
    //@NotNull
    private String login;

    //@Email
    @NotNull
    private String email;
    private LocalDate birthday;
}
