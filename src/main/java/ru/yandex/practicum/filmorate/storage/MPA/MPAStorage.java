package ru.yandex.practicum.filmorate.storage.MPA;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MPAStorage {
    MPA addMPA(MPA mpa);

    MPA deleteMPA(int id);

    Optional<MPA> findMPA(int id);

    MPA updateMPA(MPA mpa);

    List<MPA> getAllMPA();

    MPA findMPAofFilm(int filmID);
}
