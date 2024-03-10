package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;


import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {

        User newUser = new User("vanya123", LocalDate.of(1990, 1, 1));
        newUser.setEmail("user@email.ru");
        newUser.setName("Ivan Petrov");
        newUser.setId(1);

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(newUser);


        User savedUser = userStorage.getUserById(newUser.getId());
        System.out.println(savedUser.getId());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testGetAllUser() {

        User newUserF = new User("vanya123", LocalDate.of(1990, 1, 1));
        newUserF.setEmail("user@email.ru");
        newUserF.setName("Ivan Petrov");
        User newUserS = new User("danya123", LocalDate.of(1991, 2, 2));
        newUserS.setEmail("user1@email.ru");
        newUserS.setName("Danya Petrov");
        Collection<User> allUser = new ArrayList<>();
        allUser.add(newUserF);
        allUser.add(newUserS);
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.addUser(newUserF);
        userStorage.addUser(newUserS);


        Collection<User> savedUser = userStorage.allUser();

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(allUser);
    }

    @Test
    public void testChangeUser() {

        User newUserF = new User("daw123", LocalDate.of(1990, 1, 1));
        newUserF.setEmail("da2334@email.ru");
        newUserF.setName("Ivan asd");
        newUserF.setId(1);
        User newUserS = new User("dawasdasd123", LocalDate.of(1990, 1, 1));
        newUserS.setEmail("da233sdsad4@email.ru");
        newUserS.setName("Ivan asd");

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = userStorage.addUser(newUserF);
        newUserS.setId(user.getId());
        userStorage.changeUser(newUserS);


        User savedUser = userStorage.getUserById(newUserS.getId());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUserS);

    }

    @Test
    public void testAddFriend() {

        User newUserF = new User("daw123", LocalDate.of(1990, 1, 1));
        newUserF.setEmail("da2334@email.ru");
        newUserF.setName("Ivan asd");
        newUserF.setId(1);
        User newUserS = new User("dawasdasd123", LocalDate.of(1990, 1, 1));
        newUserS.setEmail("da233sdsad4@email.ru");
        newUserS.setName("Ivan asd");
        newUserS.setId(2);

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        UserService userService = new UserService(userStorage);
        userStorage.addUser(newUserF);
        userStorage.addUser(newUserS);
        userStorage.addFriend(new Friend(newUserF.getId(), newUserS.getId()));


        Integer friendId = userService.allFriends(newUserF.getId()).stream()
                .map(User::getId)
                .filter((id -> id == newUserS.getId()))
                .collect(Collectors.toList()).get(0);

        assertThat(friendId)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUserS.getId());

    }

    @Test
    public void testDeleteFriend() {

        User newUserF = new User("daw123", LocalDate.of(1990, 1, 1));
        newUserF.setEmail("da2334@email.ru");
        newUserF.setName("Ivan asd");
        newUserF.setId(1);
        User newUserS = new User("dawasdasd123", LocalDate.of(1990, 1, 1));
        newUserS.setEmail("da233sdsad4@email.ru");
        newUserS.setName("Ivan asd");
        newUserS.setId(2);

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        UserService userService = new UserService(userStorage);
        userStorage.addUser(newUserF);
        userStorage.addUser(newUserS);
        userStorage.addFriend(new Friend(newUserF.getId(), newUserS.getId()));


        Integer friendId = userService.allFriends(newUserF.getId()).stream()
                .map(User::getId)
                .filter((id -> id == newUserS.getId()))
                .collect(Collectors.toList()).get(0);

        assertThat(friendId)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUserS.getId());
        User deleteFriend = userStorage.deleteFriend(newUserF.getId(), newUserS.getId());
        boolean res = deleteFriend.getFriends().isEmpty();
        assertThat(res)
                .isNotNull()
                .isEqualTo(true);

    }

    @Test
    public void testCommonFriend() {

        User newUserF = new User("vanya123", LocalDate.of(1990, 1, 1));
        newUserF.setEmail("user@email.ru");
        newUserF.setName("Ivan Petrov");
        User newUserS = new User("danya123", LocalDate.of(1991, 2, 2));
        newUserS.setEmail("user1@email.ru");
        newUserS.setName("Danya Petrov");
        User newUserT = new User("Commondanya123", LocalDate.of(1991, 2, 2));
        newUserT.setEmail("user2@email.ru");
        newUserT.setName("Danya coomon");

        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        UserService userService = new UserService(userStorage);
        User userF = userStorage.addUser(newUserF);
        User userS = userStorage.addUser(newUserS);
        User userT = userStorage.addUser(newUserT);
        userStorage.addFriend(new Friend(userF.getId(), userT.getId()));
        userStorage.addFriend(new Friend(userS.getId(), userT.getId()));
        Collection<User> commonUser = new ArrayList<>();
        commonUser.add(userT);

        assertThat(commonUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userService.getCommonFriends(userF.getId(), userF.getId()));
    }


}

