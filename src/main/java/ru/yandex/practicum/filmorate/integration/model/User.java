package ru.yandex.practicum.filmorate.integration.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    private final int id;
    private String name;

    @NotBlank(message = "Неверный формат login")
    private String login;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Неверный формат email")
    private String email;

    @Past(message = "Этой даты еще не было")
    private LocalDate birthday;
}
