package com.zonglinpeng.litcode.dao;

import com.zonglinpeng.litcode.model.Question;
import com.zonglinpeng.litcode.model.UserReactionQuestion;
import io.vertx.core.Future;
import io.vertx.sqlclient.RowSet;
import io.vertx.core.json.JsonObject;

public interface UserDAOI {
    Future<RowSet<JsonObject>> createUpdateQuestion(JsonObject json);
    Future<RowSet<JsonObject>> getNewQuestionId();
    Future<RowSet<Question>> userRelatedQuestion(int userID, String tagName);
    Future<RowSet<Question>> deleteQuestion(int questionsID);

}
