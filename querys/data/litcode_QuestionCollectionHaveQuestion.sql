create table QuestionCollectionHaveQuestion
(
    question_collection_id int not null,
    question_id            int not null,
    constraint QuestionCollectionHaveQuestion_QuestionCollection_id_fk
        foreign key (question_collection_id) references QuestionCollection (id),
    constraint QuestionCollectionHaveQuestion_Question_id_fk
        foreign key (question_id) references Question (id)
);

