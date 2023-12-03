package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> userMap = new HashMap<>();
    private int countId = 1;

    @GetMapping
    public Collection<User> allUser() {
        log.info("Получен запрос к эндпоинту: GET");
        return userMap.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            user.setId(countId++);
            userMap.put(user.getId(), user);
            log.info("Получен запрос к эндпоинту: POST");
            return user;
        } else {
            user.setId(countId++);
            userMap.put(user.getId(), user);
            log.info("Получен запрос к эндпоинту: POST");
            return user;
        }
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        if (userMap.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                userMap.put(user.getId(), user);
                log.info("Получен запрос к эндпоинту: PUT");
                return user;
            }
            userMap.put(user.getId(), user);
            log.info("Получен запрос к эндпоинту: PUT");
            return user;
        } else {
            throw new ValidationException("Такого пользователя не существует");
        }
    }


}
