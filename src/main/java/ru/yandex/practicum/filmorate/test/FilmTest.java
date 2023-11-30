package ru.yandex.practicum.filmorate.test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {

    private Validator validator;
    private static FilmController filmController;

    @BeforeAll
    public static void beforeAll() {
        filmController = new FilmController();
    }

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void postTest() {
        Film film1 = new Film("Описание", LocalDate.of(2021, 12, 12), 12);
        Film film2 = new Film("“Забытый мир” - это захватывающий приключенческий фильм, действие которого разворачивается в постапокалиптическом мире. В центре сюжета - группа отважных героев, пытающихся разгадать тайну древнего пророчества и спасти остатки человечества от гибели.\n" +
                "\n" +
                "Главный герой, молодой воин по имени Дамиан, отправляется в опасное путешествие, чтобы найти мистический артефакт, способный вернуть равновесие в разрушенный мир. На своем пути он встречает множество препятствий и врагов, но также обретает верных друзей и союзников.\n" +
                "\n" +
                "Фильм наполнен зрелищными боями, неожиданными сюжетными поворотами и глубоким философским подтекстом. Зрители окажутся в мире, где каждый выбор героя имеет последствия и может изменить ход истории.\n" +
                "\n" +
                "“Забытый мир” подарит незабываемые эмоции всем любителям качественных фэнтези-фильмов и заставит задуматься о ценности человеческой жизни и важности принятия верных решений.", LocalDate.of(2021, 12, 12), 12);
        Film film3 = new Film("Описание", LocalDate.of(1895, 12, 12), 12);
        Film film4 = new Film("Описание", LocalDate.of(1896, 12, 12), -1);

        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        assertFalse(violations1.isEmpty());

        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);
        assertFalse(violations2.isEmpty());

        Set<ConstraintViolation<Film>> violations3 = validator.validate(film3);
        assertFalse(violations3.isEmpty());

        Set<ConstraintViolation<Film>> violations4 = validator.validate(film4);
        assertFalse(violations4.isEmpty());

        Film film = new Film("Описание", LocalDate.of(2023, 12, 12), 12);
        filmController.addFilm(film);
        assertNotEquals(0, filmController.allFilm());

    }


    @Test
    public void putTest() {
        Throwable exc = assertThrows(ValidationException.class, () -> filmController.changeFilm(new Film("Описание", LocalDate.of(2023, 12, 12), 12)));
        assertEquals("Такого фильма не существует", exc.getMessage());

        Film film = new Film("Описание", LocalDate.of(2023, 12, 12), 12);
        Film newFilm = new Film("Описание", LocalDate.of(2023, 12, 12), 12);
        filmController.addFilm(film);
        newFilm.setId(film.getId());
        filmController.changeFilm(newFilm);
        assertTrue(filmController.allFilm().contains(newFilm));
    }


}
