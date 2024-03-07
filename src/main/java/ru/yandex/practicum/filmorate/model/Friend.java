package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friend {
    private int id;
    private final int idUser;
    private final int idUserFriend;
    private boolean status = false;
}
