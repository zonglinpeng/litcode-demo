create schema litcode collate utf8mb4_general_ci;
use litcode;

create table Question
(
    id           int auto_increment
        primary key,
    title        varchar(200)  not null,
    description  text          not null,
    code_content text          not null,
    hint         text          not null,
    difficulty   varchar(50)   not null,
    likes        int default 0 not null,
    dislikes     int default 0 not null
);

create table QuestionTest
(
    id             int auto_increment
        primary key,
    contributor_id int  not null,
    content        text not null
);

create table QuestionTestTestQuestion
(
    question_test_id int not null,
    question_id      int not null,
    constraint QuestionTestTestQuestion_QuestionTest_id_fk
        foreign key (question_test_id) references QuestionTest (id)
        on delete cascade on update cascade,
    constraint QuestionTestTestQuestion_Questions_id_fk
        foreign key (question_id) references Question (id)
        on delete cascade on update cascade,
    PRIMARY KEY(question_test_id, question_id)
);

create table Tag
(
    tag_name varchar(100)
        primary key
);

create table TagHaveQuestion
(
    question_id int not null,
    tag_name      varchar(100) not null,
    constraint TagHaveQuestion_Question_id_fk
        foreign key (question_id) references Question (id)
        on delete cascade on update cascade,
    constraint TagHaveQuestion_Tag_name_fk
        foreign key (tag_name) references Tag (tag_name)
        on delete cascade on update cascade,
    PRIMARY KEY(question_id)
);

create table User
(
    id             int auto_increment
        primary key,
    oauth_id       varchar(200) not null,
    oauth_platform varchar(100) not null,
    refresh_token  varchar(500) not null
);

create table QuestionCollection
(
    id      int auto_increment
        primary key,
    name    varchar(200) not null,
    user_id int          not null,
    constraint QuestionCollection_User_id_fk
        foreign key (user_id) references User (id)
        on delete cascade on update cascade
);

create table QuestionCollectionHaveQuestion
(
    question_collection_id int not null,
    question_id            int not null,
    constraint QuestionCollectionHaveQuestion_QuestionCollection_id_fk
        foreign key (question_collection_id) references QuestionCollection (id)
        on delete cascade on update cascade,
    constraint QuestionCollectionHaveQuestion_Question_id_fk
        foreign key (question_id) references Question (id)
        on delete cascade on update cascade,
    PRIMARY KEY(question_collection_id, question_id)
);

create table UserContributeQuestion
(
    user_id     int not null,
    question_id int not null,
    constraint UserContributeQuestion_Question_id_fk
        foreign key (question_id) references Question (id)
        on delete cascade on update cascade,
    constraint UserContributeQuestion_User_id_fk
        foreign key (user_id) references User (id)
        on delete cascade on update cascade,
    PRIMARY KEY(user_id, question_id)
);

create table UserReactionQuestion
(
    user_id       int         not null,
    question_id   int         not null,
    reaction_type varchar(50) not null,
    constraint UserLikeQuestion_Questions_id_fk
        foreign key (question_id) references Question (id)
        on delete cascade on update cascade,
    constraint UserLikeQuestion_User_id_fk
        foreign key (user_id) references User (id)
        on delete cascade on update cascade,
    PRIMARY KEY(user_id, question_id)
);
