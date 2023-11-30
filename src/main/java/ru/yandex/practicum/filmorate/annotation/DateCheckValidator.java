package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateCheckValidator implements ConstraintValidator<DateCheck, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value.isAfter(LocalDate.of(1895, 12, 28))) {
            return true;
        }
        return false;
    }
}
