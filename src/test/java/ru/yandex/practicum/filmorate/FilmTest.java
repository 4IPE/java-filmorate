package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {

    private Validator validator;
    private static FilmController filmController;


    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    public void postTestFail() {
        Film film1 = new Film("Описание", LocalDate.of(2021, 12, 12), 12);
        Film film2 = new Film("“Забытый мир” - это захватывающий приключенческий фильм, действие которого разворачивается в постапокалиптическом мире. В центре сюжета - группа отважных героев, пытающихся разгадать тайну древнего пророчества и спасти остатки человечества от гибели.\n" +
                "\n" +
                "Главный герой, молодой воин по имени Дамиан, отправляется в опасное путешествие, чтобы найти мистический артефакт, способный вернуть равновесие в разрушенный мир. На своем пути он встречает множество препятствий и врагов, но также обретает верных друзей и союзников.\n" +
                "\n" +
                "Фильм наполнен зрелищными боями, неожиданными сюжетными поворотами и глубоким философским подтекстом. Зрители окажутся в мире, где каждый выбор героя имеет последствия и может изменить ход истории.\n" +
                "\n" +
                "“Забытый мир” подарит незабываемые эмоции всем любителям качественных фэнтези-фильмов и заставит задуматься о ценности человеческой жизни и важности принятия верных решений.", LocalDate.of(2021, 12, 12), 12);
        Film film3 = new Film("Описание", LocalDate.of(1895, 12, 12), 12);
        Film film4 = new Film("Описание", LocalDate.of(1895, 12, 31), -1);
        film2.setName("dan");
        film3.setName("dan");
        film4.setName("dan");

        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        assertFalse(violations1.isEmpty());
        violations1.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(val -> assertTrue((val.contains("Название не может быть пустым"))));


        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);
        assertFalse(violations2.isEmpty());
        violations2.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(val -> assertTrue((val.contains("Описание должно быть не больше 200 слов"))));


        Set<ConstraintViolation<Film>> violations3 = validator.validate(film3);
        assertFalse(violations3.isEmpty());
        violations3.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(val -> assertTrue((val.contains("Дата создания не может быть позже 28.12.1895"))));


        Set<ConstraintViolation<Film>> violations4 = validator.validate(film4);
        assertFalse(violations4.isEmpty());
        violations4.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(val -> assertTrue((val.contains("Продолжительность должна быть не меньше 1"))));
    }

    @Test
    public void postTestSuccess() {
        Film film = new Film("Описание", LocalDate.of(2023, 12, 12), 12);
        film.setName("name");
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film);
        assertTrue(violations1.isEmpty());
        filmController.addFilm(film);
        assertFalse(filmController.allFilm().isEmpty());
    }


    @Test
    public void putTestSuccess() {
        Film film = new Film("Описание", LocalDate.of(2023, 12, 12), 12);
        Film newFilm = new Film("Описание", LocalDate.of(2023, 12, 12), 12);
        filmController.addFilm(film);
        newFilm.setId(film.getId());
        filmController.changeFilm(newFilm);
        assertTrue(filmController.allFilm().contains(newFilm));
    }

    @Test
    public void putTestFail() {
        Throwable exc = assertThrows(ValidationException.class, () -> filmController.changeFilm(new Film("Описание", LocalDate.of(2023, 12, 12), 12)));
        assertEquals("Такого фильма не существует", exc.getMessage());
    }


}
