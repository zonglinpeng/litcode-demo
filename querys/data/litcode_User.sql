create table User
(
    id             int auto_increment
        primary key,
    oauth_id       varchar(200) not null,
    oauth_platform varchar(100) not null,
    refresh_token  varchar(500) not null
);

INSERT INTO litcode.User (id, oauth_id, oauth_platform, refresh_token) VALUES (1, '', '', '');