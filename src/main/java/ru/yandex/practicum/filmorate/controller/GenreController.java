package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenreByID(@PathVariable Integer id) {
        return genreService.findGenre(id);
    }

    @GetMapping
    public List<Genre> getUsers() {
        return genreService.getAllGenre();
    }
}
