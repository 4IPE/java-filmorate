package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> allUser() {
        return userStorage.allUser();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }


    public User addUser(User user) {
        return userStorage.addUser(user);
    }


    public User changeUser(User user) {
        return userStorage.changeUser(user);
    }

    public Collection<User> allFriends(int idUser) {
        if (userStorage.getUserById(idUser) != null) {
            return userStorage.getUserById(idUser).getFriends().stream()
                    .map(userStorage::getUserById)
                    .collect(Collectors.toList());
        }
        throw new NotFoundException(User.class, idUser);
    }

    public User addFriends(int mainUserId, int friendId) {
        User mainUser = userStorage.getUserById(mainUserId);
        User friend = userStorage.getUserById(friendId);
        if (userStorage.getUserById(mainUserId) == null) {
            throw new NotFoundException(User.class, mainUserId);
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException(User.class, friendId);
        }
        mainUser.getFriends().add(friendId);
        friend.getFriends().add(mainUserId);
        return userStorage.getUserById(mainUserId);
    }

    public User removeFriends(int mainUserId, int friendId) {
        if (userStorage.getUserById(mainUserId) == null) {
            throw new NotFoundException(User.class, mainUserId);
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException(User.class, friendId);
        }
        if (!userStorage.getUserById(mainUserId).getFriends().contains(friendId)) {
            throw new ValidationException("У пользователя с ID:" + mainUserId + "нету друга с ID" + friendId);
        }
        userStorage.getUserById(mainUserId).getFriends().remove(friendId);
        return userStorage.getUserById(mainUserId);
    }

    public Collection<User> checkCommonFriends(int mainUserID, int otherId) {
        return allFriends(mainUserID).stream()
                .filter(user -> allFriends(otherId).contains(user))
                .collect(Collectors.toList());

    }

}
