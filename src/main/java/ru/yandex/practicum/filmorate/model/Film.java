package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.DateRelease;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Film.
 */
@Data
@Builder(toBuilder = true)
public class Film {
    private final int id;

    @NotBlank(message = "Имя фильма не может быть пустым")
    private String name;

    @Size(min = 0, max = 200, message = "Максимальная длина описания фильма - 200 символов")
    @NotBlank(message = "У фильма должно быть описание")
    private String description;

    @DateRelease
    private LocalDate releaseDate;

    @PositiveOrZero(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @PositiveOrZero
    private int rate;

    @NotNull
    private MPA mpa;

    private final Set<Genre> genres = new LinkedHashSet<>();
//            new TreeSet<>(Comparator.comparing(Genre::getId, Comparator.naturalOrder()));

    public void likeFilm() {
        rate++;
    }

    public void unlikeFilm() {
        rate--;
    }

    public void addGenre(Set<Genre> genres) {
        this.genres.addAll(genres);
    }

    public void deleteGenre(Set<Genre> genres) {
        this.genres.removeAll(genres);
    }

}
