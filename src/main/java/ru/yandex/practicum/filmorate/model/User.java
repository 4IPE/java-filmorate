package ru.yandex.practicum.filmorate.model;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Data
public class User {
    private final int id;
    @Email
    private  String email;
    private  String name;
    private final String login;
    private final LocalDate birthday;

}
