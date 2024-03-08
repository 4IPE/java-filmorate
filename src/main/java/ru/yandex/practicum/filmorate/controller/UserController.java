package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/users")
    public Collection<User> allUser() {
        log.info("Получен запрос к эндпоинту: GET");
        return userService.allUser();
    }

    @GetMapping("/users/{id}")
    public User userById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: GET");
        return userService.getUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> allFriendsByUser(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту: GET");
        return userService.allFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: POST");
        return userService.addUser(user);

    }

    @PutMapping("/users")
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Получен запрос к эндпоинту: PUT");
        return userService.changeUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос к эндпоинту: PUT");
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос к эндпоинту: DELETE");
        return userService.removeFriends(id, friendId);
    }


}
