package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> userMap = new HashMap<>();
    private int countId = 1;


    public Collection<User> allUser() {
        return userMap.values();
    }

    public User getUserById(int id) {
        if (userMap.get(id) == null) {
            throw new NotFoundException(User.class, id);
        }
        return userMap.get(id);
    }


    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            user.setId(countId++);
            userMap.put(user.getId(), user);
            return user;
        } else {
            user.setId(countId++);
            userMap.put(user.getId(), user);
            return user;
        }
    }


    public User changeUser(User user) {
        if (userMap.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                userMap.put(user.getId(), user);
                return user;
            }
            userMap.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException(User.class, user.getId());
        }
    }
}
