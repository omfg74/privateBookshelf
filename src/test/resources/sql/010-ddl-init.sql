create table book
(
    id        bigserial    not null
        constraint book_pk
            primary key,
    name      varchar(100) not null,
    author_id bigint,
    image_url varchar(200),
    outlet    varchar(20)
);

create table file
(
    id             bigserial not null
        constraint file_pk
            primary key,
    name           text      not null,
    file_extension varchar(5),
    book_id bigint
);

create table author
(
    id         bigserial not null
        constraint author_pk
            primary key,
    first_name varchar(100),
    last_name  varchar(100)
);

create table book_to_author
(
    author_id bigserial,
    book_id   bigserial
);


create table genre
(
    id   bigserial not null
        constraint genre_pk
            primary key,
    name varchar(40)
);

create table book_to_genre
(
    genre_id bigint,
    book_id  bigint
);
