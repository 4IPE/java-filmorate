package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExceedingDate;
import ru.yandex.practicum.filmorate.exception.ExceedingTheLimit;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/filmorate")
@Slf4j

public class FilmController {
    private Map<Integer, Film> filmMap = new HashMap<>();


    @GetMapping
    public Map<Integer, Film> allFilm() {
        log.info("Получен запрос к эндпоинту: GET");
        return filmMap;
    }

    @PostMapping
    public void addFilm(@RequestBody Film film) {
        if (film.getDescription().length() <= 200) {
            if (film.getDuration() > 0) {
                if (film.getReleaseDate().isAfter(LocalDate.parse("28.12.1895", DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
                    filmMap.put(film.getId(), film);
                    log.info("Получен запрос к эндпоинту: Post");
                } else {
                    log.warn("Получен запрос к эндпоинту: Post , Возникает ошибка: Некоректная дата ");
                    throw new ExceedingDate("Некоректная дата");
                }
            } else {
                log.warn("Получен запрос к эндпоинту: Post , Возникает ошибка: Продолжительность должна быть положительной");
                throw new IllegalArgumentException("Продолжительность должна быть положительной");
            }
        } else {
            log.warn("Получен запрос к эндпоинту: Post , Возникает ошибка: Превышен лимит описания");
            throw new ExceedingTheLimit("Превышен лимит описания ");
        }
    }

    @PutMapping
    public void changeFilm(@RequestBody Film film, HttpServletRequest request) {
        if (film.getDescription().length() <= 200) {
            if (film.getDuration() > 0) {
                if (film.getReleaseDate().isAfter(LocalDate.parse("28.12.1895", DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
                    filmMap.put(film.getId(), film);
                    log.info("Получен запрос к эндпоинту: Post");
                } else {
                    log.warn("Получен запрос к эндпоинту: Post , Возникает ошибка: Некоректная дата ");
                    throw new ExceedingDate("Некоректная дата");
                }
            } else {
                log.warn("Получен запрос к эндпоинту: Post , Возникает ошибка: Продолжительность должна быть положительной");
                throw new IllegalArgumentException("Продолжительность должна быть положительной");
            }
        } else {
            log.warn("Получен запрос к эндпоинту: Post , Возникает ошибка: Превышен лимит описания");
            throw new ExceedingTheLimit("Превышен лимит описания ");
        }
    }
}
