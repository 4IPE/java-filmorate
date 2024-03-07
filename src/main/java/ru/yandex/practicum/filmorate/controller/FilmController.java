package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    @GetMapping("/films")
    public Collection<Film> allFilm() {
        log.info("Получен запрос к эндпоинту: GET");
        return filmService.allFilm();
    }

    @GetMapping("/films/{id}")
    public Film filmById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: GET");
        return filmService.getFilmByID(id);
    }

    @GetMapping("films/popular")
    public Collection<Film> popularFilm(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.popularFilm(count);
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: POST");
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос к эндпоинту: PUT");
        return filmService.changeFilm(film);
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

    @GetMapping("/genres")
    public Collection<Genre> allGenres() {
        log.info("Получен запрос к эндпоинту: GET");
        return filmService.allGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: GET");
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> allMpa() {
        log.info("Получен запрос к эндпоинту: GET");
        return filmService.allMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: GET");
        return filmService.getMpaById(id);
    }


}
