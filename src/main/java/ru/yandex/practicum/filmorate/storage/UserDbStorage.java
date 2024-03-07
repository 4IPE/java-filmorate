package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.*;


@Component
@Qualifier("userDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> allUser() {
        Collection<User> userList = jdbcTemplate.query("SELECT u.* FROM USERS u", userRowMapper());
        return userList;
    }

    @Override
    public User getUserById(int id) {
        return jdbcTemplate.queryForObject("SELECT u.*  FROM USERS u  WHERE u.id_user = ?", userRowMapper(), id);
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("id_user");
        Map<String, Object> params = Map.of(
                "email", user.getEmail(),
                "name", user.getName(),
                "login", user.getLogin(),
                "birthday", user.getBirthday());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        if (!user.getFriends().isEmpty()) {
            for (Integer idFriend : user.getFriends()) {
                addFriend(new Friend(user.getId(), idFriend));
            }
        }
        return user;
    }

    @Override
    public Friend addFriend(Friend friend) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("friend")
                .usingGeneratedKeyColumns("id_friend");
        Map<String, Object> params = Map.of("id_user", friend.getId_user(),
                "id_user_friend", friend.getId_user_friend(),
                "status", friend.isStatus());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        friend.setId(id.intValue());
        return friend;
    }


    @Override
    public User changeUser(User user) {
        final String sql = "UPDATE users SET email = ?,login = ? ,name = ?,birthday = ? WHERE id_user = ?";
        int update = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        if (update <= 0) {
            throw new NotFoundException(User.class, user.getId());
        }
        return user;
    }

    @Override
    public User deleteFriend(int mainUserId, int friendId) {
        final String sqlFriend = "DELETE FROM friend WHERE id_user =? AND id_user_friend = ? ";
        int update = jdbcTemplate.update(sqlFriend, mainUserId, friendId);
        return getUserById(mainUserId);
    }


    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User(rs.getString("login"), rs.getDate("birthday").toLocalDate());
            int id = rs.getInt("id_user");
            user.setId(id);
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            jdbcTemplate.query("SELECT * FROM friend  AS f WHERE f.id_user = ?", (rs1, rowNum1) -> {
                try {
                    do {
                        user.getFriends().add(rs1.getInt("id_user_friend"));

                    } while (rs1.next());
                    return user;
                } catch (SQLException e) {
                    return user;
                }
            }, user.getId());
            return user;
        };
    }


}
