package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundError;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
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
            throw new NotFoundError(Film.class, id);
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
            throw new NotFoundError(Film.class, film.getId());
        }

    }
}
