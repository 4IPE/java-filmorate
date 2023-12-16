package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundError;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class UserTest {

    private static UserController userController;
    private Validator validator;
    private static UserStorage userStorage;
    private static UserService userService;


    @BeforeAll
    public static void beforeAll() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userStorage, userService);
    }

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void postTest() {
        User user2 = new User("dan", LocalDate.of(2021, 12, 12));
        User user3 = new User("    ", LocalDate.of(2021, 12, 12));
        User user4 = new User("dan", LocalDate.of(2025, 12, 12));
        user3.setEmail("dam22@mail.ru");
        user4.setEmail("dam22@mail.ru");
        user2.setEmail("@FDSGF");

        Set<ConstraintViolation<User>> violations1 = validator.validate(user2);
        assertFalse(violations1.isEmpty());
        violations1.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(val -> assertTrue(val.contains("Некорректный email")));


        Set<ConstraintViolation<User>> violations2 = validator.validate(user3);
        assertFalse(violations2.isEmpty());
        violations2.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(val -> assertTrue(val.contains("Логин не может быть путсым")));


        Set<ConstraintViolation<User>> violations3 = validator.validate(user4);
        assertFalse(violations3.isEmpty());
        violations3.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(val -> assertTrue(val.contains("День рождения не может быть в будущем")));

    }

    @Test
    public void postTestSuccess() {
        User user = new User("dan", LocalDate.of(2021, 12, 12));
        user.setEmail("dan123@mail.ru");
        Set<ConstraintViolation<User>> violations1 = validator.validate(user);
        assertTrue(violations1.isEmpty());
        userController.addUser(user);
        assertFalse(userController.allUser().isEmpty());
    }

    @Test
    public void putTestSuccess() {
        User user2 = new User("dan", LocalDate.of(2021, 12, 12));
        userController.addUser(user2);
        User newUser = new User("danNew", LocalDate.of(2021, 12, 12));
        newUser.setId(user2.getId());
        userController.changeUser(newUser);
    }

    @Test
    public void putTestFail() {
        Throwable exc = assertThrows(NotFoundError.class, () -> userController.changeUser(new User("dan", LocalDate.of(2021, 12, 12))));
        assertEquals("Userс индефикатором 0 не найден", exc.getMessage());
    }
}
