create table QuestionTestTestQuestion
(
    question_test_id int not null,
    question_id      int not null,
    constraint QuestionTestTestQuestion_QuestionTest_id_fk
        foreign key (question_test_id) references QuestionTest (id),
    constraint QuestionTestTestQuestion_Questions_id_fk
        foreign key (question_id) references Question (id)
);

