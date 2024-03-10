package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.PossibleActionException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;


    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
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
        if (!film.getLikesOfUsers().contains(userId)) {
            film.getLikesOfUsers().add(userId);
            film.setLike(film.getLikesOfUsers().size());
            return filmStorage.addLike(filmId, userId);
        }
        throw new ValidationException("Нельзя ставить несколько лайков одному фильму");
    }

    public Film dislike(int filmId, int userId) {
        Film film = filmStorage.getFilmByID(filmId);
        if (film == null) {
            throw new NotFoundException(Film.class, filmId);
        }
        if (film.getLike() > 0) {
            film.getLikesOfUsers().remove(userId);
            film.setLike(film.getLikesOfUsers().size());
            return filmStorage.deleteLike(filmId, userId);
        }
        throw new PossibleActionException("Нельзя убирать лайк тому посту которому не был поставлен лайк");
    }

    public Collection<Film> popularFilm(Integer size) {
        return filmStorage.allFilm().stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }

    public Collection<Genre> allGenre() {
        return filmStorage.allGenre();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public Collection<Mpa> allMpa() {
        return filmStorage.allMpa();
    }

    public Mpa getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }


}
