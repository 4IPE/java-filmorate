package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameNullValidator implements ConstraintValidator<NameNull, User> {
    @Override
    public boolean isValid(User u, ConstraintValidatorContext constraintValidatorContext) {
        if (u.getName().isBlank() || u.getName().isEmpty()) {
            u.setName(u.getLogin());
            return true;
        }
        return true;
    }


}
