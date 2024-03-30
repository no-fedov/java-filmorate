# java-filmorate

![ER Diagram SQL](https://github.com/no-fedov/java-filmorate/blob/main/ER%20Diagram.png)

Table user {
  id integer [pk] 
  email varchar 
  login varchar
  name varchar
  birthday date
}

Table friendship {
  user_id integer [ref: > user.id]
  friend_id integer [ref: > user.id]

  indexes {
    (user_id,friend_id) [pk] 
  }
}


Table film {
  id integer [pk]
  name varchar
  description varchar
  duration integer
  release_date date
  rating_id integer [ref: > rating.id]
}

Table film_genre {
  film_id integer [ref: > film.id]
  genre_id integer [ref: > genre.id]

  indexes {
    (film_id, genre_id) [pk]
  }
}

Table genre {
  id integer [pk]
  name varchar
}

Table rating {
  id integer [pk]
  name varchar
}

Table like_mark_film {
  film_id integer [ref: > film.id]
  user_id integer [ref: > user.id]

  indexes {
  (film_id,user_id) [pk]
  }
}
