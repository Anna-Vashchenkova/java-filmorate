create table if not exists mpas
(
    id   int generated by default as identity primary key,
    name varchar(5) not null
);

create table if not exists films
(
    id           int generated by default as identity primary key,
    name         varchar(50) not null,
    description  varchar(200),
    release_date date not null,
    duration     int not null,
    rating       int references mpas(id)
);

create table if not exists genres
(
    id   int generated by default as identity primary key,
    name varchar(25) not null
);

create table if not exists film_genres
(
    id          int generated by default as identity primary key,
    film_id     int,
    genre_id    int,
    foreign key (film_id) references films (id) on delete cascade,
    foreign key (genre_id) references genres (id) on delete cascade
);

create table if not exists users
(
    id       int generated by default as identity primary key,
    name     varchar(125),
    login    varchar(25)    not null,
    birthday date           not null,
    email    varchar(25)    not null
);

create table if not exists friends
(
    id       int generated by default as identity primary key,
    user_id   int,
    friend_id int,
    friendship_status varchar(10),
    foreign key (user_id) references users (id) on delete cascade,
    foreign key (friend_id) references users (id) on delete cascade
);

create table if not exists likes
(
    id       int generated by default as identity primary key,
    film_id int,
    user_id int,
    foreign key (film_id) references films (id) on delete cascade,
    foreign key (user_id) references users (id) on delete cascade
);

