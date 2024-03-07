package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friend {
    private int id;
    private final int id_user;
    private final int id_user_friend;
    private boolean status = false;
}
