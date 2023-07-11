package com.bamboovir.litcode.dao;

import com.bamboovir.litcode.model.Question;
import com.bamboovir.litcode.model.UserReactionQuestion;
import io.vertx.core.Future;
import io.vertx.sqlclient.RowSet;
import io.vertx.core.json.JsonObject;

public interface UserDAOI {
    Future<RowSet<JsonObject>> createUpdateQuestion(JsonObject json);
    Future<RowSet<JsonObject>> getNewQuestionId();
    Future<RowSet<Question>> userRelatedQuestion(int userID, String tagName);
    Future<RowSet<Question>> deleteQuestion(int questionsID);

}
