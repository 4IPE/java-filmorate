package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> allUser();

    public User getUserById(int id);

    public User addUser(User user);

    public User changeUser(User user);

    public Friend addFriend(Friend friend);

    User deleteFriend(int mainUserId, int friendId);


}
