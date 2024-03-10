package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> filmMap = new HashMap<>();
    private int countId = 1;

    public Collection<Film> allFilm() {
        return filmMap.values();
    }


    public Film getFilmByID(int id) {
        if (filmMap.get(id) == null) {
            throw new NotFoundException(Film.class, id);
        }
        return filmMap.get(id);
    }

    public Film addFilm(Film film) {
        film.setId(countId++);
        filmMap.put(film.getId(), film);
        return film;
    }

    public Film changeFilm(Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException(Film.class, film.getId());
        }
    }

    public Collection<Genre> allGenre() {
        return null;
    }

    public Collection<Mpa> allMpa() {
        return null;
    }

    public Genre getGenreById(int id) {
        return null;
    }

    public Mpa getMpaById(int id) {
        return null;
    }

    public Film addLike(int filmId, int userId) {
        return null;
    }

    public Film deleteLike(int filmId, int userId) {
        return null;
    }

    public List<User> getLikeForFilm(int id) {
        return null;
    }
}
