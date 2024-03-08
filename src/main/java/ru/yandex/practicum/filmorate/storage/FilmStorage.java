package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> allFilm();

    Film addFilm(Film film);

    Film changeFilm(Film film);

    Film getFilmByID(int id);

    Collection<Genre> allGenre();

    Collection<Mpa> allMpa();

    Genre getGenreById(int id);

    Mpa getMpaById(int id);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    List<User> getLikeForFilm(int id);
}
