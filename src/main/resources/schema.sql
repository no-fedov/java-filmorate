CREATE TABLE IF NOT EXISTS genre (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS rating_mpa (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_filmorate (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email VARCHAR NOT NULL UNIQUE,
  login VARCHAR NOT NULL,
  name VARCHAR,
  birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friendship (
  user_id INTEGER NOT NULL REFERENCES user_filmorate (id) ON DELETE CASCADE,
  friend_id INTEGER NOT NULL REFERENCES user_filmorate (id) ON DELETE CASCADE,
  PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS film (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL,
  description VARCHAR(200),
  duration INTEGER CHECK (duration > 0),
  release_date DATE NOT NULL CHECK (release_date > '1895-12-28'),
  rate INTEGER NOT NULL,
  rating_id INTEGER REFERENCES rating_mpa (id)
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
  genre_id INTEGER REFERENCES genre (id),
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS like_mark_film (
  film_id INTEGER REFERENCES film (id) ON DELETE CASCADE,
  user_id INTEGER REFERENCES user_filmorate (id) ON DELETE CASCADE,
  PRIMARY KEY (film_id, user_id)
);



