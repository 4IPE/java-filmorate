package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.PossibleActionException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> allFilm() {
        return filmStorage.allFilm();
    }


    public Film getFilmByID(int id) {
        return filmStorage.getFilmByID(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film changeFilm(Film film) {
        return filmStorage.changeFilm(film);

    }

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmByID(filmId);
        if (film == null) {
            throw new NotFoundException(Film.class, filmId);
        }
        if (!film.getLikesOfUsers().contains(userStorage.getUserById(userId))) {
            film.setLike(film.getLike() + 1);
            film.getLikesOfUsers().add(userStorage.getUserById(userId));
            return film;
        }
        throw new ValidationException("Нельзя ставить несколько лайков одному фильму");
    }

    public Film dislike(int filmId, int userId) {
        Film film = filmStorage.getFilmByID(filmId);
        if (film == null) {
            throw new NotFoundException(Film.class, filmId);
        }
        if (film.getLike() > 0) {
            film.setLike(film.getLike() - 1);
            film.getLikesOfUsers().remove(userStorage.getUserById(userId));
            return film;
        }
        throw new PossibleActionException("Нельзя убирать лайк тому посту которому не был поставлен лайк");
    }

    public Collection<Film> popularFilm(Integer size) {
        return filmStorage.allFilm().stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }


}
