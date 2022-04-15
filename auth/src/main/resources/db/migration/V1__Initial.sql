CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username varchar(50) not null unique,
    password varchar not null,
    email varchar (100) not null,
    enabled boolean not null,
    isAdmin boolean not null default false
);
