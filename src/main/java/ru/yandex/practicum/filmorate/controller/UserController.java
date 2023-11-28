package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    Map<Integer,User> userMap = new HashMap<>();

    @GetMapping
    public Map<Integer,User> allUser() {
        log.info("Получен запрос к эндпоинту: GET");
        return userMap;
    }

    @PostMapping
    public void addUser(@Valid @RequestBody  User user) {
            if(user.getLogin()!=null && !user.getLogin().isBlank()){
                if(user.getBirthday().isBefore(LocalDate.now())){
                    if(user.getName()==null || user.getName().isBlank()){
                        user.setName(user.getLogin());
                        userMap.put(user.getId(), user);
                        log.info("Получен запрос к эндпоинту: Post");
                    }
                    else{
                        userMap.put(user.getId(), user);
                        log.info("Получен запрос к эндпоинту: Post");
                    }
                }
                else{
                    throw new ValidationException("День рождения не может быть в будущем");
                }
            }
            else {
                log.warn("Получен запрос к эндпоинту: Post,Возникшая ошибка: Некоректный login");
                throw new ValidationException("Некоректный login");
            }
        }

    @PutMapping
    public void changeUser(@Valid @RequestBody  User user,HttpServletRequest request) {
        if(user.getEmail().contains("@")) {
            if(user.getLogin()!=null && !user.getLogin().isBlank()){
                if(user.getBirthday().isBefore(LocalDate.now())){
                    if(user.getName()==null || user.getName().isBlank()){
                        user.setName(user.getLogin());
                        userMap.put(user.getId(), user);
                        log.info("Получен запрос к эндпоинту: Post");
                    }
                    else{
                        userMap.put(user.getId(), user);
                        log.info("Получен запрос к эндпоинту: Post");
                    }
                }
            }
            else {
                log.warn("Получен запрос к эндпоинту: Post,Возникшая ошибка: Некоректный login");
                throw new ValidationException("Некоректный login");
            }
        }
        else {
            log.warn("Получен запрос к эндпоинту: Post,Возникшая ошибка: Некоректный email");
            throw new ValidationException("Некоректный email");
        }
    }


}
