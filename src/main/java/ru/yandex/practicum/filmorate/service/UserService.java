package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {


    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
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
        if (mainUser == null) {
            throw new NotFoundException(User.class, mainUserId);
        }
        if (friend == null) {
            throw new NotFoundException(User.class, friendId);
        }
        mainUser.getFriends().add(friendId);
        mainUser.setStatus(true);
        Friend friendObj = new Friend(mainUserId, friendId);
        userStorage.addFriend(friendObj);
        if (friend.getFriends().contains(mainUserId)) {
            friend.getFriends().add(mainUserId);
            friendObj.setStatus(true);
            userStorage.addFriend(friendObj);
        }
        return userStorage.getUserById(mainUserId);
    }

    public User removeFriends(int mainUserId, int friendId) {
        User mainUser = userStorage.getUserById(mainUserId);
        User friend = userStorage.getUserById(friendId);
        if (mainUser == null) {
            throw new NotFoundException(User.class, mainUserId);
        }
        if (friend == null) {
            throw new NotFoundException(User.class, friendId);
        }
        userStorage.getUserById(mainUserId).getFriends().remove(friendId);
        userStorage.deleteFriend(mainUserId, friendId);
        return userStorage.getUserById(mainUserId);
    }

    public Collection<User> getCommonFriends(int mainUserID, int otherId) {
        return allFriends(mainUserID).stream()
                .filter(user -> allFriends(otherId).contains(user))
                .collect(Collectors.toList());

    }

}
