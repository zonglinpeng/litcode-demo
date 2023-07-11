package com.zonglinpeng.litcode.dao;

import com.zonglinpeng.litcode.model.Question;
import com.zonglinpeng.litcode.util.Transform;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.List;
import java.util.Map;

public class UserMySQL implements UserDAOI {
    private final MySQLPool client;

    public UserMySQL(MySQLPool client) {
        this.client = client;
    }


    public Future<RowSet<JsonObject>> createUpdateQuestion(JsonObject json) {
        String title = json.getString("title");
        String description = json.getString("description");
        String code_content = json.getString("code_content");
        String hint = json.getString("hint");
        String difficulty = json.getString("difficulty");
        String tagName = json.getString("tagName");
        String questionID = json.getString("questionID");
        String isUpdate = json.getString("isUpdate");
        System.out.printf("createQuestion fields, title=%s, description=%s, tagName=%s, questionID=%s, isUpdate=%b\n", title, "\""+description+"\"", tagName, questionID, isUpdate);

        int question_id = (questionID.equals("new")) ? -1 : Integer.parseInt(questionID);

        String trans_procedure = "call create_update_transaction( " +
                " #{isUpdate}, #{title}, #{description}, #{code_content}, #{hint}, #{difficulty}, #{question_id}, #{tagName}, " +
                " @qid, @tag_name ) ; "
                ;

        return SqlTemplate
                .forUpdate(client, trans_procedure)
                .mapTo(Row::toJson)
                .execute(Map.of("isUpdate", isUpdate,"title", title, "description", description , "code_content",
                         code_content, "hint", hint, "difficulty", difficulty, "tagName", tagName, "question_id", question_id));

    }


    public Future<RowSet<JsonObject>> getNewQuestionId() {
        String select_maxid = "SELECT max(id) as max_id FROM Question";

        return SqlTemplate
                .forQuery(client, select_maxid)
                .mapTo(Row::toJson)
                .execute(Map.of());
    }

    public Future<RowSet<Question>> userRelatedQuestion(int userID, String tagName) {
        String unionTemplate = "( " +
                "SELECT Q.id, Q.title, Q.difficulty " +
                "FROM Question Q JOIN TagHaveQuestion H on(Q.id = H.question_id) JOIN Tag T on (H.tag_name = T.tag_name) JOIN UserReactionQuestion R using (question_id) " +
                "WHERE T.tag_name = #{tagName} and R.reaction_type = 'like' and R.user_id = #{userID} " +
                ") " +
                "UNION " +
                "( " +
                "SELECT Q.id, Q.title, Q.difficulty " +
                " FROM Question Q JOIN TagHaveQuestion H on(Q.id = H.question_id) JOIN Tag T on (H.tag_name = T.tag_name) JOIN UserContributeQuestion C using (question_id) " +
                " WHERE T.tag_name = #{tagName} and C.user_id = #{userID} " +
                ")";

        return SqlTemplate
                .forQuery(client, unionTemplate)
                .mapTo(Question.class)
                .execute(Map.of("userID", userID, "tagName", tagName));
    }

    public Future<RowSet<Question>> deleteQuestion(int questionID) {
        String queryTemplate =
                "SELECT * " +
                        "FROM Question " +
                        "WHERE id = #{questionID}";

        String deleteTemplate =
                "DELETE FROM Question " +
                        "WHERE id = ?";

        Future<RowSet<Question>> entryToDelete = SqlTemplate
                .forQuery(client, queryTemplate)
                .mapTo(Question.class)
                .execute(Map.of("questionID", questionID));

        this.client.preparedQuery(deleteTemplate)
                .execute(
                        Tuple.of(String.format("%d", questionID)),
                        ar -> {
                            if (ar.succeeded()) {
                                RowSet<Row> rows = ar.result();
                                System.out.println(rows.rowCount());
                            } else {
                                System.out.println("Failure: " + ar.cause().getMessage());
                            }
                        });

        return entryToDelete;
    }

}
