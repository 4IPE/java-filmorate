package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> allFilm();

    public Film addFilm(Film film);

    public Film changeFilm(Film film);

    public Film getFilmByID(int id);
}
