drop table IF exists films, users,likesOfUsers, friend,genresFilm,genresName,ratingFilm,ratingName;

CREATE TABLE IF NOT exists films (
  id_film integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar UNIQUE,
  description varchar,
  releaseDate timestamp,
  duration integer
);

CREATE TABLE IF NOT exists  users (
  id_user integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email varchar,
  name varchar,
  login varchar UNIQUE,
  birthday timestamp
);

CREATE TABLE IF NOT exists likesOfUsers (
  id_user integer not null references users(id_user) on delete cascade,
  id_film integer not null references films(id_film ) on delete cascade
);

CREATE TABLE IF NOT exists  friend(
  id_friend integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  id_user integer,
  id_user_friend integer,
  status boolean
);
CREATE TABLE IF NOT exists genresName (
  id_genres integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar UNIQUE
);

CREATE TABLE IF NOT exists genresFilm (
  id_genres integer not null references genresName(id_genres ) on delete cascade,
  id_film integer not null references films(id_film ) on delete cascade
);

CREATE TABLE IF NOT exists ratingName (
  id_rating integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  rating_name varchar UNIQUE
);

CREATE TABLE IF NOT exists ratingFilm(
  id_rating integer not null references ratingName(id_rating ) on delete cascade,
  id_film integer not null references films(id_film ) on delete cascade
);



ALTER TABLE likesOfUsers ADD FOREIGN KEY (id_film) REFERENCES films (id_film);


ALTER TABLE friend ADD FOREIGN KEY (id_user) REFERENCES users (id_user);

ALTER TABLE friend ADD FOREIGN KEY (id_user_friend) REFERENCES users (id_user);

ALTER TABLE genresFilm ADD FOREIGN KEY (id_film) REFERENCES films (id_film);

ALTER TABLE genresFilm ADD FOREIGN KEY (id_genres) REFERENCES genresName (id_genres);

ALTER TABLE ratingFilm ADD FOREIGN KEY (id_film) REFERENCES films (id_film);

ALTER TABLE ratingFilm ADD FOREIGN KEY (id_rating) REFERENCES ratingName (id_rating);