package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MPAService {

    private final MPAStorage mpaStorage;

    public MPA findMPA(int id) {
        MPA mpa = mpaStorage.findMPA(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id = " + id + " не найден"));
        log.info("Обработан запрос по поиску рейтинга MPA. Найден рейтинг MPA: {}", mpa);
        return mpa;
    }

    public List<MPA> getAllMPA() {
        List<MPA> listMPA = mpaStorage.getAllMPA();
        log.info("Обработан запрос на получение списка всех рейтингов MPA. Найдены рейтинг MPA: {}", listMPA);
        return listMPA;
    }
}
