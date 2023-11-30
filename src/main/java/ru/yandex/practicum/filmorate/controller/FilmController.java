package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j

public class FilmController {
    private Map<Integer, Film> filmMap = new HashMap<>();
    private int countId = 1;

    @GetMapping
    public Collection<Film> allFilm() {
        log.info("Получен запрос к эндпоинту: GET");
        return filmMap.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(countId++);
        filmMap.put(film.getId(), film);
        log.info("Получен запрос к эндпоинту: POST");
        return film;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Получен запрос к эндпоинту: PUT");
            return film;
        } else {
            throw new ValidationException("Такого фильма не существует");
        }

    }
}
