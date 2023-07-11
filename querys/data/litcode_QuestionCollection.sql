create table QuestionCollection
(
    id      int auto_increment
        primary key,
    name    varchar(200) not null,
    user_id int          not null,
    constraint QuestionCollection_User_id_fk
        foreign key (user_id) references User (id)
);

