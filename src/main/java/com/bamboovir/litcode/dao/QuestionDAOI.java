package com.zonglinpeng.litcode.dao;

import com.zonglinpeng.litcode.model.Question;
import com.zonglinpeng.litcode.model.TagHaveQuestion;
import com.zonglinpeng.litcode.model.UserReactionQuestion;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.core.json.JsonObject;
import java.util.List;


public interface QuestionDAOI {
    Future<RowSet<Question>> listQuestions(int start, int step);
    Future<RowSet<Row>> deleteLikeQuestion(String reactType, int questionsID, int userID);
    Future<RowSet<UserReactionQuestion>> checkReactExists(int user_id, int question_id, String react_type);
    Future<RowSet<Question>> getReactCnt(int question_id);
    Future<RowSet<UserReactionQuestion>> getReactToQuestion(int question_id);
    Future<RowSet<Row>> reactToQuestion(int user_id, int question_id, String react_type, int update);
    Future<RowSet<Question>> searchQuestions(String keyWords);
    Future<RowSet<Question>> updateQuestions(int questionID, Question question);
    Future<RowSet<Question>> searchQuestionID(int questionID);
    Future<RowSet<JsonObject>> AdvancedQuery1();
    Future<String> createTag(int questionID, String tagName);
    Future<String> updateTag(int questionID, String tagName);
}

