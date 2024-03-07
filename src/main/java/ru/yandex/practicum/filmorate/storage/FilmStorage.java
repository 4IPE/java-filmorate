package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    public Collection<Film> allFilm();

    public Film addFilm(Film film);

    public Film changeFilm(Film film);

    public Film getFilmByID(int id);

    public Collection<Genre> allGenre();

    public Collection<Mpa> allMpa();

    public Genre getGenreById(int id);

    public Mpa getMpaById(int id);

    public Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    List<User> getLikeForFilm(int id);
}
