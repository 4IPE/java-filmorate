package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAllFilm() {
        Film filmF = new Film("Description 1 ", LocalDate.of(1900, 12, 1), 100);
        Film filmS = new Film("Description 2 ", LocalDate.of(1911, 12, 1), 100);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmF.setName("FilmF");
        filmS.setName("FilmS");
        filmF.setMpa(filmDbStorage.getMpaById(1));
        filmS.setMpa(filmDbStorage.getMpaById(2));
        filmDbStorage.addFilm(filmF);
        filmDbStorage.addFilm(filmS);

        Collection<Film> allFilm = new ArrayList<>();
        allFilm.add(filmF);
        allFilm.add(filmS);

        Collection<Film> allFilmInBd = filmDbStorage.allFilm();
        assertThat(allFilmInBd)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(allFilm);
    }

    @Test
    public void testGetFilmById() {
        Film filmF = new Film("Description 1 ", LocalDate.of(1900, 12, 1), 100);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmF.setName("FilmF");
        filmF.setMpa(filmDbStorage.getMpaById(1));
        filmDbStorage.addFilm(filmF);

        Film filmByID = filmDbStorage.getFilmByID(filmF.getId());
        assertThat(filmByID)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmF);
    }

    @Test
    public void testChangeFilm() {
        Film filmF = new Film("Description 1 ", LocalDate.of(1900, 12, 1), 100);
        Film filmS = new Film("Description 2 ", LocalDate.of(1911, 12, 1), 100);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmF.setName("FilmF");
        filmS.setName("FilmS");
        filmF.setMpa(filmDbStorage.getMpaById(1));
        filmS.setMpa(filmDbStorage.getMpaById(2));
        Film film = filmDbStorage.addFilm(filmF);
        filmS.setId(filmF.getId());
        Film updFilm = filmDbStorage.changeFilm(filmS);


        assertThat(filmF)
                .isNotNull()
                .usingRecursiveComparison()
                .isNotEqualTo(updFilm);
    }

    @Test
    public void testFunctionLike() {
        Film filmF = new Film("Description 1 ", LocalDate.of(1900, 12, 1), 100);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmF.setName("FilmF");
        filmF.setMpa(filmDbStorage.getMpaById(1));
        filmDbStorage.addFilm(filmF);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User newUserF = new User("daw123", LocalDate.of(1990, 1, 1));
        newUserF.setEmail("da2334@email.ru");
        newUserF.setName("Ivan asd");
        newUserF.setId(1);
        User user = userDbStorage.addUser(newUserF);
        Film filmLike = filmDbStorage.addLike(filmF.getId(), user.getId());
        Integer userLikeId = filmLike.getLikesOfUsers().stream()
                .filter(id -> id == user.getId()).collect(Collectors.toList()).get(0);

        assertThat(userLikeId)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user.getId());
        Film filmDelete = filmDbStorage.deleteLike(filmF.getId(), newUserF.getId());
        boolean res = filmDelete.getLikesOfUsers().isEmpty();
        assertThat(res)
                .isNotNull()
                .isEqualTo(true);
    }

}
