package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> allUser();

    User getUserById(int id);

    User addUser(User user);

    User changeUser(User user);

    void addFriend(Friend friend);

    User deleteFriend(int mainUserId, int friendId);


}
