package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping
@Slf4j

public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> allFilm() {
        log.info("Получен запрос к эндпоинту: GET");
        return filmStorage.allFilm();
    }

    @GetMapping("/films/{id}")
    public Film filmById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: GET");
        return filmStorage.getFilmByID(id);
    }

    @GetMapping("films/popular")
    public Collection<Film> popularFilm(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.popularFilm(count);
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: POST");
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: PUT");
        return filmStorage.changeFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос к эндпоинту: PUT");
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос к эндпоинту: DELETE");
        return filmService.dislike(id, userId);
    }


}
