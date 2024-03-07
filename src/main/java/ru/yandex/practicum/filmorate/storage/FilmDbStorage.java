package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Qualifier("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> allFilm() {
        return jdbcTemplate.query("SELECT f.*,rg.* FROM FILMS f JOIN RATINGFILM AS r ON r.ID_FILM =f.ID_FILM JOIN RATINGNAME AS rg ON rg.ID_RATING =r.ID_RATING",
                filmRowMapper());
    }

    @Override
    public Film getFilmByID(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM FILMS AS f JOIN RATINGFILM AS r ON r.ID_FILM =f.ID_FILM JOIN RATINGNAME AS rg ON rg.ID_RATING =r.ID_RATING WHERE f.ID_FILM =?",
                filmRowMapper(), id);

    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id_film");

        Map<String, Object> params = Map.of("name", film.getName(),
                "description", film.getDescription(),
                "releasedate", film.getReleaseDate(),
                "duration", film.getDuration());
        Number id_film = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id_film.intValue());
        if (film.getMpa() != null) {
            Mpa mpa = jdbcTemplate.queryForObject("select * from ratingname where id_rating = ?",
                    (rs, rowNum) -> new Mpa(rs.getInt("id_rating"), rs.getString("rating_name")), film.getMpa().getId());
            film.setMpa(mpa);
            SimpleJdbcInsert simpleJdbcInsertRating = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("ratingfilm");
            Map<String, Integer> paramsRating = Map.of("id_film", film.getId(),
                    "id_rating", film.getMpa().getId());
            simpleJdbcInsertRating.execute(paramsRating);
        }
        if (!film.getGenres().isEmpty()) {
            SimpleJdbcInsert simpleJdbcInsertGenre = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("genresfilm");
            Map<String, Integer> paramsGenre = new HashMap<>();
            for (Genre genre : film.getGenres()) {
                paramsGenre.put("id_film", film.getId());
                paramsGenre.put("id_genres", genre.getId());
            }
            simpleJdbcInsertGenre.execute(paramsGenre);
            List<Genre> genres = jdbcTemplate.query("select * from genresFilm as gf join genresName as gn on gf.id_genres = gn.id_genres where gf.id_film =?",
                    genreRowMapper(), film.getId());
            film.setGenres(new HashSet<>(genres));

        }
        return film;
    }

    @Override
    public Film changeFilm(Film film) {
        if (getFilmByID(film.getId()) == null) {
            throw new NotFoundException(Film.class, film.getId());
        }
        final String sql = "UPDATE films SET name = ?,description = ? ,releaseDate = ?,duration = ? WHERE id_film = ?";
        final String sqlMpa = "UPDATE ratingFilm SET id_rating =? WHERE id_film = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        if (film.getMpa() != null) {
            Mpa mpa = jdbcTemplate.queryForObject("select * from ratingname where id_rating = ?", mpaRowMapper(), film.getMpa().getId());
            film.setMpa(mpa);
            jdbcTemplate.update(sqlMpa, film.getMpa().getId(), film.getId());
        }
        if (!film.getGenres().isEmpty()) {
            jdbcTemplate.update("DELETE FROM genresFilm AS g WHERE g.id_film = ?", film.getId());
            Set<Genre> genres = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource()).withTableName("genresFilm");
                Map<String, Integer> params = Map.of("id_genres", genre.getId(), "id_film", film.getId());
                simpleJdbcInsert.execute(params);
                genres.add(genre);
            }
            film.setGenres(new HashSet<>(genres));
        } else {
            jdbcTemplate.update("DELETE FROM genresFilm AS g WHERE g.id_film = ?", film.getId());
            film.setGenres(new HashSet<>());
        }

        return getFilmByID(film.getId());
    }

    @Override
    public Collection<Genre> allGenre() {
        return jdbcTemplate.query("select * from genresName AS g ORDER BY g.ID_GENRES ", genreRowMapper());
    }

    @Override
    public Collection<Mpa> allMpa() {
        return jdbcTemplate.query("select * from ratingName AS r ORDER BY r.id_rating", mpaRowMapper());
    }

    @Override
    public Genre getGenreById(int id) {
        return jdbcTemplate.queryForObject("select * from genresName where id_genres = ?", genreRowMapper(), id);
    }

    @Override
    public Mpa getMpaById(int id) {
        return jdbcTemplate.queryForObject("select * from ratingName where id_rating = ?", mpaRowMapper(), id);
    }

    @Override
    public Film addLike(int filmId, int userId) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("likesOfUsers");
        Map<String, Integer> params = Map.of("id_user", userId,
                "id_film", filmId);
        simpleJdbcInsert.execute(params);
        return getFilmByID(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        final String sql = "DELETE FROM likesOfUsers WHERE id_user =? AND id_film = ? ";
        int update = jdbcTemplate.update(sql, userId, filmId);
        return getFilmByID(filmId);
    }

    @Override
    public List<User> getLikeForFilm(int id) {
        return jdbcTemplate.query("select * from likeOfUsers where id_film = ?", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
                return userDbStorage.getUserById(rs.getInt("id_user"));
            }
        }, id);
    }

    private RowMapper<Film> filmRowMapper() {

        return (rs, rowNum) -> {
            Film film = new Film(rs.getString("description"), rs.getDate("releaseDate").toLocalDate(), rs.getInt("duration"));
            film.setName(rs.getString("name"));
            film.setId(rs.getInt("id_film"));
            film.setMpa(new Mpa(rs.getInt("id_rating"), rs.getString("rating_name")));
            List<Integer> id = jdbcTemplate.query("SELECT * FROM GENRESFILM AS g  WHERE g.ID_FILM =?", (rs1, rowNum1) -> rs1.getInt("id_genres"), film.getId());
            Set<Genre> genres = new HashSet<>();
            for (int elem : id) {
                genres.add(getGenreById(elem));
            }
            List<Genre> genresSort = new ArrayList<>(genres);
            genresSort.sort(Comparator.comparingInt(Genre::getId));
            film.setGenres(new HashSet<>(genresSort));
            List<Integer> idUser = jdbcTemplate.query("SELECT * FROM likesOfUsers AS l  WHERE l.ID_FILM =?", (rs2, rowNum2) -> rs2.getInt("id_user"), film.getId());
            film.setLikesOfUsers(new HashSet<>(idUser));
            film.setLike(film.getLikesOfUsers().size());
            return film;
        };
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> {
            Genre genre = new Genre(rs.getInt("id_genres"), rs.getString("name"));
            return genre;
        };
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(rs.getInt("id_rating"), rs.getString("rating_name"));
    }
}
