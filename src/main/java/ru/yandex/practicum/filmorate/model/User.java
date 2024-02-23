package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    private final int id;
    private String name;

    @NotBlank(message = "Неверный формат login")
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не должен содержать пробелов")
    private String login;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Неверный формат email")
    private String email;

    @PastOrPresent(message = "Этой даты еще не было")
    @NotNull
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();
}
