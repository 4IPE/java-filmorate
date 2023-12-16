package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundError;
import ru.yandex.practicum.filmorate.exception.PossibleAction;
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

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmByID(filmId);
        if (film == null) {
            throw new NotFoundError(Film.class, filmId);
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
            throw new NotFoundError(Film.class, filmId);
        }
        if (film.getLike() > 0) {
            film.setLike(film.getLike() - 1);
            film.getLikesOfUsers().remove(userStorage.getUserById(userId));
            return film;
        }
        throw new PossibleAction("Нельзя убирать лайк тому посту которому не был поставлен лайк");
    }

    public Collection<Film> popularFilm(Integer size) {
        return filmStorage.allFilm().stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }


}
