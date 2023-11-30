package ru.yandex.practicum.filmorate.test;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class UserTest {

    private static UserController userController;
    private Validator validator;


    @BeforeAll
    public static void beforeAll() {
        userController = new UserController();
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

        user2.setEmail("@FDSGF");
        Set<ConstraintViolation<User>> violations1 = validator.validate(user2);
        assertFalse(violations1.isEmpty());


        Set<ConstraintViolation<User>> violations2 = validator.validate(user3);
        assertFalse(violations2.isEmpty());


        Set<ConstraintViolation<User>> violations3 = validator.validate(user4);
        assertFalse(violations3.isEmpty());

        User user = new User("dan", LocalDate.of(2021, 12, 12));
        userController.addUser(user);
        assertFalse(userController.allUser().isEmpty());

    }

    @Test
    public void putTest() {
        User user2 = new User("dan", LocalDate.of(2021, 12, 12));
        userController.addUser(user2);
        User newUser = new User("danNew", LocalDate.of(2021, 12, 12));
        newUser.setId(user2.getId());
        userController.changeUser(newUser);

        Throwable exc = assertThrows(ValidationException.class, () -> userController.changeUser(new User("dan", LocalDate.of(2021, 12, 12))));
        assertEquals("Такого пользователя не существует", exc.getMessage());
    }
}
