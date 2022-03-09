CREATE TABLE IF NOT EXISTS users(
    id bigint(20) NOT NULL AUTO_INCREMENT primary key,
    username varchar(50) not null,
    password varchar not null,
    email varchar (100) not null,
    enabled boolean not null,
    isAdmin boolean not null default false,
    UNIQUE KEY uk_users_username (username)
);
