package com.bamboovir.litcode.dao;

import com.bamboovir.litcode.model.Question;
import com.bamboovir.litcode.model.UserReactionQuestion;
import com.bamboovir.litcode.model.TagHaveQuestion;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.List;
import java.util.Map;

public class QuestionMySQL implements QuestionDAOI {
    private final MySQLPool client;

    public QuestionMySQL(MySQLPool client) {
        this.client = client;
    }

    public Future<RowSet<Question>> listQuestions(int start, int step) {
        String template = "SELECT * " +
                "FROM Question " +
                "LIMIT #{start}, #{step}";

        return SqlTemplate
                .forQuery(client, template)
                .mapTo(Question.class)
                .execute(Map.of("start", start, "step", step));

    }


    public Future<RowSet<UserReactionQuestion>> checkReactExists(int user_id, int question_id, String react_type) {
        // select * from UserReactionQuestion where user_id = [user_id] AND question_id = [question_id] AND reaction_type = [react_type];
        String query = "SELECT * from UserReactionQuestion WHERE " +
                "user_id = #{user_id} AND question_id = #{question_id}";
        System.out.printf("checkReactExists query : %s\n", query);
        return SqlTemplate
                .forQuery(client, query)
                .mapTo(UserReactionQuestion.class)
                .execute(Map.of("user_id", user_id, "question_id", question_id, "react_type", react_type));
    }

    public Future<RowSet<Question>> getReactCnt(int question_id){
        String template = "SELECT likes, dislikes " +
                "FROM Question " +
                "WHERE id = #{question_id}";

        return SqlTemplate
                .forQuery(client, template)
                .mapTo(Question.class)
                .execute(Map.of("question_id", question_id));
    }

    public Future<RowSet<UserReactionQuestion>> getReactToQuestion(int question_id){
        String template = "SELECT reaction_type " +
                "FROM UserReactionQuestion " +
                "WHERE user_id=1 and question_id = #{question_id}";

        return SqlTemplate
                .forQuery(client, template)
                .mapTo(UserReactionQuestion.class)
                .execute(Map.of("question_id", question_id));
    }


    public Future<RowSet<Row>> deleteLikeQuestion(String reactType, int questionID, int userID) {
        String deleteTemplate =
                "DELETE FROM UserReactionQuestion " +
                        "WHERE user_id = ? " +
                        "AND question_id = ? " +
                        "AND reaction_type = ? ";

        return this.client.preparedQuery(deleteTemplate)
                .execute(
                        Tuple.of(
                                String.format("%d", userID),
                                String.format("%d", questionID),
                                reactType
                        )
                );
    }
    public Future<RowSet<Row>> reactToQuestion(int user_id, int question_id, String react_type, int update) {
        /*
         * arguments:
         * @react_type : use for reaction_type in UserReactionQuestion table
         *           "like" , "dislike"
         * @update:
         *           1: update
         *           0: insert
         * variables:
         *          q_react_type = react_type + "s"
         *          q_reverse_type: reverse reaction of proposed one
         * Note: only need to handle UserReactionQuestion, trigger takes care of the likes and dislikes count in Question table
         */
        String update_reaction;
        String q_reverse_type = react_type.equals("like") ? "dislikes" : "likes";
        String q_react_type = react_type + "s";
        System.out.printf("request question react_type=%s, question reverse_type=%s\n", q_react_type, q_reverse_type);

        if (update == 1) {
            update_reaction = String.format("UPDATE UserReactionQuestion SET reaction_type='%s' WHERE user_id=%d AND question_id=%d", react_type, user_id, question_id);
        } else { // insert
            update_reaction = String.format("INSERT INTO UserReactionQuestion VALUES (%d, %d, '%s')", user_id, question_id, react_type);
        }

        System.out.printf("Within reactToQuestion(), update_reaction clause: %s\n", update_reaction);

        return this.client.withTransaction(client -> client
                .query(update_reaction)
                .execute());
    }

    public Future<RowSet<Question>> searchQuestions(String keyWords) {
        String template = "SELECT * " +
                "FROM Question " +
                "WHERE title LIKE concat('%', #{keyWords}, '%')";

        return SqlTemplate
                .forQuery(client, template)
                .mapTo(Question.class)
                .execute(Map.of("keyWords", keyWords));

    }

    public Future<RowSet<Question>> updateQuestions(int questionID, Question question) {
        String template = "UPDATE Question " +
                "SET title = #{title}, " +
                "description = #{newDescription}, " +
                "code_content = #{code_content}, " +
                "hint = #{hint}, " +
                "difficulty = #{difficulty} " +
                "WHERE id = #{questionID}";

        return SqlTemplate
                .forUpdate(client, template)
                .mapTo(Question.class)
                .execute(Map.of("title", question.title, "newDescription", question.description,
                        "code_content", question.code_content, "hint", question.hint,
                        "difficulty", question.difficulty, "questionID", questionID));
    }

    public Future<RowSet<Question>> searchQuestionID(int questionID) {
        String template = "SELECT * " +
                "FROM Question " +
                "WHERE id = #{questionID}";

        return SqlTemplate
                .forQuery(client, template)
                .mapTo(Question.class)
                .execute(Map.of("questionID", questionID));
    }

    public Future<RowSet<JsonObject>> AdvancedQuery1() {
        String template = "SELECT H.tag_name, T.tag_name, SUM(Q.likes) Likes\n" +
                "FROM TagHaveQuestion H JOIN Question Q ON(Q.id=H.question_id) JOIN Tag T ON(T.tag_name = H.tag_name)\n" +
                "GROUP BY H.tag_name\n" +
                "HAVING Likes >= all(\n" +
                " SELECT SUM(Q1.likes)\n" +
                " FROM TagHaveQuestion H1 JOIN Question Q1 ON(Q1.id=H1.question_id)\n" +
                " GROUP BY H1.tag_name\n" +
                ");";

        return SqlTemplate
                .forQuery(client, template)
                .mapTo(Row::toJson)
                .execute(Map.of());
    }

    public Future<String> createTag(int questionID, String tagName) {
        String updateQuery = String.format(
                "INSERT INTO TagHaveQuestion (question_id, tag_name) "+
                "VALUES (%d, '%s')",
                questionID, 
                tagName);

        return this.client.withTransaction(client -> client
                .query(updateQuery)
                .execute()
                .map("Tag created"));
                
        // return SqlTemplate
        //         .forUpdate(client, template)
        //         .mapTo(TagHaveQuestion.class)
        //         .execute(Map.of("tag_name", tagName, "question_id", questionID))
        //         .onSuccess(v -> System.out.println("Update succeeded"))
        //         .onFailure(err -> System.out.println("Update failed: " + err.getMessage()));
        // return this.client
        //         .query(updateSQL)
        //         .execute(
        //                 Tuple.of(
        //                         String.format("%d", questionID),
        //                         tagName
        //                 ))
        //         .onSuccess(v -> System.out.println("Update succeeded"))
        //         .onFailure(err -> System.out.println("Update failed: " + err.getMessage()));
    }

    public Future<String> updateTag(int questionID, String tagName) {
//        String template = "UPDATE TagHaveQuestion " +
//                "SET tag_name = #{tagName} " +
//                "WHERE question_id = #{questionID}";

        String updateTrans =
                "START TRANSACTION; " +
                ""
                        ;
         String updateQuery = String.format(
                 "UPDATE TagHaveQuestion " +
                 "SET tag_name = '%s' " +
                 "WHERE question_id = %d",
                 tagName,
                 questionID);

         return this.client.withTransaction(client -> client
                 .query(updateQuery)
                 .execute()
                 .map("Tag updated"));

//        TagHaveQuestion tagHaveQuestion = new TagHaveQuestion();
//        tagHaveQuestion.setQuestion_id(questionID);
//        tagHaveQuestion.setTag_name(tagName);

//        return SqlTemplate
//                .forUpdate(client, template)
//                .mapTo(TagHaveQuestion.class)
//                .execute(Map.of("tag_name", tagHaveQuestion.tag_name, "question_id", tagHaveQuestion.question_id))
//                .onSuccess(v -> System.out.println("Update succeeded"))
//                .onFailure(err -> System.out.println("Update failed: " + err.getMessage()));
    }
}
